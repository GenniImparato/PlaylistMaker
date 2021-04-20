<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
	

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="/PlaylistMaker/css/style.css">
		<title>Post Example</title>
	</head>

<body>
	<div class="main_div">
		<h1 class="title">Playlist Maker</h1>
		<h2>Welcome 
			<span style='color:orange'>
				<c:out value="${currentUser.username}"></c:out>
			</span>
			, this is your home page!
		</h2>
		
		<div class="button_div">
			<a class="button" href="/PlaylistMaker/logout"> Log out </a>
		</div>
		
		<p></p>
		
		<div class="list_div">
			<h2>Your playlists:</h2>
			
			<c:choose>
				<c:when test="${playlists.size()>0}">
					<c:forEach var="pl" items="${playlists}">
						<button class="button" href="/PlaylistMaker/HomePage"><c:out value="${pl.name}"/></button>
						
					</c:forEach>
				</c:when>
				<c:otherwise>
					<p>No playlists.</p>
				</c:otherwise>
			</c:choose>
		</div>
		
		<div class="list_div">
			<h2>Create new playlist:</h2>
			
			<form method="post" action="/PlaylistMaker/createPlaylist">
				<label for="name"><b>Name: </b></label>
		    	<input type="text" placeholder="Enter Playlist name" name="name" required/>
		    	
		    	<div class="button_div">
		    		<input class="button" type="submit" value="Create Playlist">
		    	</div>
			</form>
		</div>
		
		<div class="list_div">
			<h2>Upload a song:</h2>
			
			<form method="post" action="/PlaylistMaker/uploadSong" enctype="multipart/form-data">
				<p>
				<label for="title"><b>Title: </b></label>
			    <input type="text" placeholder="Enter Title" name="title" required/>
		    	</p>
		    	
		    	<p>
				<label for="artist"><b>Artist: </b></label>
			    <input type="text" placeholder="Enter Artist" name="artist" required/>
		    	</p>
		    	
		    	<p>
				<label for="album"><b>Album: </b></label>
			    <input type="text" placeholder="Enter Album" name="album" required/>
		    	</p>
		    	
		    	<p>
				<label for="year"><b>Year: </b></label>
			    <input type="text" placeholder="Enter Year" name="year" required/>
		    	</p>
		    	
		    	<p>
				<label for="genre"><b>Genre: </b></label>
			    <select  id="genre" name="genre">
					    <option>Country</option>
					    <option>Hip-hop</option>
					    <option>Indie rock</option>
					    <option>Jazz</option>
					    <option>K-pop</option>
					    <option>Metal</option>
					    <option>Pop</option>
					    <option>Rap</option>
					    <option>Rhythm & blues (R&B)</option>
					    <option>Rock</option>
				</select>
		    	</p>
		    	
		    	
		    	<p>
				<label class="custom-file-upload"><b>File</b>:
					<input type="file" name ="audio" accept="audio/*">
				</label>
		    	
		    	<div class="button_div">
		    		<input class="button" type="submit" value="Upload Song">
		    	</div>
			</form>
		</div>
		
	</div>
</body>
</html>