/**
 * main.js
 */
{
	let pageOrchestrator = new PageOrchestrator(); 
	let playlistsList;
	let showPlaylist;
	let showSongs;
	let songPlayer;
	
	window.addEventListener("load", () => 
		{
			//check if logged
	    	if(sessionStorage.getItem("currentUsername") == null) 
			{
	     		window.location.href = "login.html";
	    	} 
			else 
			{
			  	pageOrchestrator.start();
			  	pageOrchestrator.refresh();
	    	} 
	  	}, false);

		  
	// Constructors of view components
	function UsernameText(username, messagecontainer) 
	{
	    this.username = username;
	    this.show = function() 
		{
	    	messagecontainer.textContent = this.username;
	    }
	}
	
	function CreatePlaylist(submitButton)
	{
		this.button = submitButton;
		
		this.button.addEventListener('click', (e) => 
		{
	    	var form = e.target.closest("form");
	    	if(form.checkValidity()) 
			{
	      		makeCall("POST", 'createPlaylist', e.target.closest("form"),
	        	function(x) 
				{
	          		if (x.readyState == XMLHttpRequest.DONE) 
					{
	            		var message = x.responseText;
						if(x.status == 200)
						{
							playlistsList.show();
						}
	            		else
						{
							alert("Error: " + message);
			            }
	          		}
	        	});
	    	} 
			else
			{
	    		form.reportValidity();
	    	}
	  	});
	}
	
	function UploadSong(submitButton)
	{
		this.button = submitButton;
		
		this.button.addEventListener('click', (e) => 
		{
	    	var form = e.target.closest("form");
	    	if(form.checkValidity()) 
			{
	      		makeCall("POST", 'uploadSong', e.target.closest("form"),
	        	function(x) 
				{
	          		if (x.readyState == XMLHttpRequest.DONE) 
					{
	            		var message = x.responseText;
						if(x.status == 200)
						{
							alert("Song uploaded!");
						}
	            		else 
						{
							alert("Error: " + message);
			            }
	          		}
	        	});
	    	} 
			else
			{
	    		form.reportValidity();
	    	}
	  	});
	}
	
	function ShowPlaylist(listContainer)  
	{
	    this.listContainer = listContainer;
		this.index = 1;
		
		this.reset = function()
		{
			this.index = 1;
		}
		
		this.update = function(playlist) 
		{
			this.playlist = playlist;
			
	      	this.listContainer.innerHTML = ""; // empty the container body
			this.listContainer.setAttribute("style", "display:block;");

			title = document.createElement("h2");
			title.textContent = "Current Playlist: " + playlist.name;
			this.listContainer.appendChild(title);
			
			table = document.createElement("table");
			this.listContainer.appendChild(table);
			
			row = document.createElement("tr");
			table.appendChild(row);


			for(var i=0; i<5; i++)
			{
				//if there is a song to show
				if((i + (this.index-1)*5) < Object.keys(playlist.songs).length)
				{
					button = document.createElement("button");
					button.setAttribute("class", "button");
					button.textContent = playlist.songs[i + (this.index-1)*5].title;
					button.songId = playlist.songs[i + (this.index-1)*5].id;
					button.addEventListener('click', (e) => 
					{
						var url = "getSongData?songId=" + e.currentTarget.songId;
						makeCall("GET", url, null,
					      	function(req) 
							{
								if (req.readyState == XMLHttpRequest.DONE) 
								{
						        	if(req.status == 200) 
									{
						              	var song = JSON.parse(req.responseText);
						              	songPlayer.update(song);
						          	} 
									else
									{
						            	alert("Error: " + req.responseText);
						          	}
								}
					        });
					});
		        	row.appendChild(button);
	
					image = document.createElement("img");
					image.setAttribute("width", 200);
					image.setAttribute("height", 200);
					var imageData = "data:image;base64," + playlist.songs[i + (this.index-1)*5].image;
					image.setAttribute("src", imageData);
					button.appendChild(image);
				}
				else
				{
					button = document.createElement("button");
					button.setAttribute("class", "button");
					button.textContent = "No song";
		        	row.appendChild(button);

					image = document.createElement("img");
					image.setAttribute("width", 200);
					image.setAttribute("height", 200);
					image.setAttribute("src", "missing_song.png");
					button.appendChild(image);
				}
				
			}
			
			div = document.createElement("div");
			div.setAttribute("class", "button_div");
			this.listContainer.appendChild(div);
			
			prev = document.createElement("button");
			prev.setAttribute("class", "button");
			prev.textContent = "Previous";
	        div.appendChild(prev);

			if(this.index > 1)
			{
				prev.addEventListener('click', (e) => 
					{
				    	this.index-=1;
						showPlaylist.update(playlist);
				  	});
			}
			else
			{
				prev.setAttribute("style", "visibility:hidden;");
			}
			

			indexText = document.createElement("p");
			indexText.textContent = this.index + "/" + (Math.trunc(playlist.songs.length/5) + 1);
			div.appendChild(indexText);

			next = document.createElement("button");
			next.setAttribute("class", "button");
			next.textContent = "Next";
	        div.appendChild(next);

			if(this.index < (Math.trunc(playlist.songs.length/5) + 1))
			{
				next.addEventListener('click', (e) => 
					{
				    	this.index+=1;
						showPlaylist.update(playlist);
				  	});
			}
			else
			{
				next.setAttribute("style", "visibility:hidden;");
			}
	    };

		this.isSongContained = function(songId)
		{
			var found = false;
			this.playlist.songs.forEach(function(song) 
			{
				if(song.id == songId)
				{
					found = true;
				}
			});
			return found;
		};
	}
	
	function PlaylistsList(messageContainer, listContainer) 
	{
		this.messageContainer = messageContainer;
	    this.listContainer = listContainer;

	    this.show = function() 
		{
	      var self = this;
	      makeCall("GET", "getPlaylists", null,
	      	function(req) 
			{
				if (req.readyState == XMLHttpRequest.DONE) 
				{
		        	if(req.status == 200) 
					{
		              	var playlists = JSON.parse(req.responseText);
		              	self.update(playlists);	            
		          	} 
					else
					{
		            	alert("Error: " + req.responseText);
		          	}
				}
	        });
	    };

		this.update = function(playlists) 
		{
	      	this.listContainer.innerHTML = ""; // empty the container body

	      	// build updated list
	      	var self = this;

			if(playlists.length == 0) 
			{
				var text = document.createElement("p");
				text.textContent = "No playlists created!";
				this.listContainer.appendChild(text);
		                	
		    }

	      	playlists.forEach(function(playlist) 
			{
				button = document.createElement("button");
				button.setAttribute("class", "button");
				button.textContent = playlist.name;
				button.addEventListener('click', (e) => 
					{
						var url = "getPlaylistData?playlistId=" + playlist.id;
						makeCall("GET", url, null,
					      	function(req) 
							{
								if (req.readyState == XMLHttpRequest.DONE) 
								{
						        	if(req.status == 200) 
									{
						              	var playlist = JSON.parse(req.responseText);
						              	if (playlists.length != 0) 
										{
											showPlaylist.reset();
						              		showPlaylist.update(playlist);
											showSongs.show();
										}
						          	} 
									else
									{
						            	alert("Error: " + req.responseText);
						          	}
								}
					        });
					});
	        	self.listContainer.appendChild(button);
	      	});
	    };
	}
	
	function ShowSongs(listContainer)
	{
		this.listContainer = listContainer;
		var self = this;
		
		this.show = function()
		{
			makeCall("GET", "getSongs", null,
		      	function(req) 
				{
					if (req.readyState == XMLHttpRequest.DONE) 
					{
			        	if(req.status == 200) 
						{
			              	var songs = JSON.parse(req.responseText);
			              	self.update(songs);
			          	} 
						else
						{
			            	alert("Error: " + req.responseText);
			          	}
					}
		        });
		}
		
		this.update = function(songs)
		{
			this.listContainer.innerHTML = ""; // empty the container body
			this.listContainer.setAttribute("style", "display:block;");

			title = document.createElement("h2");
			title.textContent = "Songs:";
			this.listContainer.appendChild(title);
			
			table = document.createElement("table");
			this.listContainer.appendChild(table);
			
			row = document.createElement("tr");
			table.appendChild(row);
			
			col = document.createElement("td");
			row.appendChild(col);
			elem = document.createElement("b");
			elem.textContent = "Title";
			col.appendChild(elem);
			
			col = document.createElement("td");
			row.appendChild(col);
			elem = document.createElement("b");
			elem.textContent = "Artist";
			col.appendChild(elem);
			
			col = document.createElement("td");
			row.appendChild(col);
			elem = document.createElement("b");
			elem.textContent = "Album";
			col.appendChild(elem);
			
			col = document.createElement("td");
			row.appendChild(col);
			elem = document.createElement("b");
			elem.textContent = "Year";
			col.appendChild(elem);
			
			col = document.createElement("td");
			row.appendChild(col);
			elem = document.createElement("b");
			elem.textContent = "Genre";
			col.appendChild(elem);
			
			col = document.createElement("td");
			row.appendChild(col);
			elem = document.createElement("b");
			elem.textContent = "Image";
			col.appendChild(elem);
			
			col = document.createElement("td");
			row.appendChild(col);
			
			songs.forEach(function(song) 
			{
				//show only songs not already in current playlist
				if(!showPlaylist.isSongContained(song.id))
				{
					row = document.createElement("tr");
					table.appendChild(row);
					
					col = document.createElement("td");
					row.appendChild(col);
					elem = document.createElement("p");
					elem.textContent = song.title;
					col.appendChild(elem);
					
					col = document.createElement("td");
					row.appendChild(col);
					elem = document.createElement("p");
					elem.textContent = song.artist;
					col.appendChild(elem);
					
					col = document.createElement("td");
					row.appendChild(col);
					elem = document.createElement("p");
					elem.textContent = song.album;
					col.appendChild(elem);
					
					col = document.createElement("td");
					row.appendChild(col);
					elem = document.createElement("p");
					elem.textContent = song.year;
					col.appendChild(elem);
					
					col = document.createElement("td");
					row.appendChild(col);
					elem = document.createElement("p");
					elem.textContent = song.genre;
					col.appendChild(elem);
					
					col = document.createElement("td");
					row.appendChild(col);
					elem = document.createElement("img");
					elem.setAttribute("width", 80);
					elem.setAttribute("height", 80);
					var imageData = "data:image;base64," + song.image;
					elem.setAttribute("src", imageData);
					col.appendChild(elem);
					
					col = document.createElement("td");
					row.appendChild(col);
					elem = document.createElement("button");
					elem.setAttribute("class", "button");
					elem.textContent = "Add to playlist";
					elem.songId = song.id;
			        col.appendChild(elem);
		
					elem.addEventListener('click', (e) => 
						{
							var url = "addSongToPlaylist?playlistId=" + showPlaylist.playlist.id + "&songId=" + e.currentTarget.songId;
							makeCall("GET", url, null,
					      	function(req) 
							{
								if (req.readyState == XMLHttpRequest.DONE) 
								{
						        	if(req.status == 200) 
									{
						              	var url = "getPlaylistData?playlistId=" + showPlaylist.playlist.id;
										makeCall("GET", url, null,
									      	function(req) 
											{
												if (req.readyState == XMLHttpRequest.DONE) 
												{
										        	if(req.status == 200) 
													{
										              	var playlist = JSON.parse(req.responseText);
										              	showPlaylist.update(playlist);
														showSongs.show();
										          	} 
													else
													{
										            	alert("Error: " + req.responseText);
										          	}
												}
					        				});
						          	} 
									else
									{
						            	alert("Error: " + req.responseText);
						          	}
								}
					        });
						});
					
					}
			});
		}	
	}
	
	function SongPlayer(container)
	{
		this.container = container;
		
		this.update = function(song)
		{
			this.container.innerHTML = ""; // empty the container body
			this.container.setAttribute("style", "display:block;");

			title = document.createElement("h2");
			title.textContent = "Song Player";
			this.container.appendChild(title);
			
			table = document.createElement("table");
			table.setAttribute("style", "width:100%;")
			this.container.appendChild(table);
			
			row = document.createElement("tr");
			table.appendChild(row);
			col = document.createElement("td");
			row.appendChild(col);
			elem = document.createElement("b");
			elem.textContent = "Title";
			col.appendChild(elem);
			col = document.createElement("td");
			row.appendChild(col);
			col.textContent = song.title;
			
			row = document.createElement("tr");
			table.appendChild(row);
			col = document.createElement("td");
			row.appendChild(col);
			elem = document.createElement("b");
			elem.textContent = "Artist";
			col.appendChild(elem);
			col = document.createElement("td");
			row.appendChild(col);
			col.textContent = song.artist;
			
			row = document.createElement("tr");
			table.appendChild(row);
			col = document.createElement("td");
			row.appendChild(col);
			elem = document.createElement("b");
			elem.textContent = "Album";
			col.appendChild(elem);
			col = document.createElement("td");
			row.appendChild(col);
			col.textContent = song.album;
			
			row = document.createElement("tr");
			table.appendChild(row);
			col = document.createElement("td");
			row.appendChild(col);
			elem = document.createElement("b");
			elem.textContent = "Year";
			col.appendChild(elem);
			col = document.createElement("td");
			row.appendChild(col);
			col.textContent = song.year;
			
			row = document.createElement("tr");
			table.appendChild(row);
			col = document.createElement("td");
			row.appendChild(col);
			elem = document.createElement("b");
			elem.textContent = "Genre";
			col.appendChild(elem);
			col = document.createElement("td");
			row.appendChild(col);
			col.textContent = song.genre;

			div = document.createElement("div");
			this.container.appendChild(div);
			image = document.createElement("img");
			image.setAttribute("width", 500);
			image.setAttribute("height", 500);
			var imageData = "data:image;base64," + song.image;
			image.setAttribute("src", imageData);
			div.appendChild(image);
			
			div = document.createElement("div");
			this.container.appendChild(div);
			sound = document.createElement('audio');
			sound.width = 500;
			sound.controls = "controls";
			sound.autoplay = "autoplay";
			sound.src = 'data:audio/ogg;base64,' + song.audio;
			div.appendChild(sound);
		}
	}
	
	function PageOrchestrator() 
	{
	    
	    this.start = function() 
		{
			//show username
			userText = new UsernameText(sessionStorage.getItem('currentUsername'), document.getElementById("id_username"));
	      	userText.show();

			createPlaylist = new CreatePlaylist(document.getElementById("create_playlist_button"));
			
			uploadSong = new UploadSong(document.getElementById("upload_song_button"));
			
			playlistsList = new PlaylistsList(document.getElementById("playlists_message"), document.getElementById("playlists_container"));
			playlistsList.show();
			
			showPlaylist = new ShowPlaylist(document.getElementById("current_playlist_div"));
			
			showSongs = new ShowSongs(document.getElementById("add_songs_div"));
			
			songPlayer = new SongPlayer(document.getElementById("song_player_div"))
			
	    };
	
	    this.refresh = function() 
		{
	    };
	}
};