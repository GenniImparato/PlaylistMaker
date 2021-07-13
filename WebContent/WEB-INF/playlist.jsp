<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="bean.Playlist"%>
	

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="/PlaylistMaker/css/style.css">
		<title>Playlist Maker</title>
	</head>

<body>
	<div class="main_div">
		<div class="button_div">
			<h1 class="title">Playlist Maker</h1>
			<a class="button" href="/PlaylistMaker/HomePage"> back to Home Page </a>
		</div>
		
		<h2>Playlist Name:  
			<c:out value="${currentPlaylist.name}"></c:out>
		</h2>
		
		<h2>Creation Date:  
			<c:out value="${currentPlaylistDate}"></c:out>
		</h2>

		<p></p>
		
		<div class="list_div">
		
		<h2>
			Songs in playlist: <c:out value="${currentPlaylist.songs.size()}"></c:out> 
		</h2>
		
			<c:choose>
				<c:when test="${currentPlaylist.songs.size()>0}">
					<table style="width:100%">
					<tr>
						<c:forEach var = "i" begin = "1" end = "5">
							<td><c:out value="${(currentIndex-1)*5 + i}"/></td>
						</c:forEach>
					</tr>
					
					<tr>
					 	<c:forEach var = "i" begin = "0" end = "4">
						 	<c:choose>
							 	<c:when test="${currentPlaylist.songs.size()>(currentIndex-1)*5 + i}">
							 		<c:set value="${currentPlaylist.songs.get((currentIndex-1)*5 + i)}" var="song" />	
							        <td><a class="button" href="/PlaylistMaker/SongPage?songId=<c:out value="${song.id}"/>">
							        	<c:out value="${song.title}"/>
							        	<img width="200" height="200" src="data:image;base64,${song.image}"/>
							        </a></td>
							      </c:when>
							      <c:otherwise>
								      <td><img width="80" height="90" src="missing_song.png"/><div>No song!</div></td>
							      </c:otherwise>
							</c:choose>
					    </c:forEach>

				    </tr>
				    
				     <tr>
					 	<c:forEach var = "i" begin = "0" end = "4">
						 	<c:choose>
						 		<c:when test="${currentPlaylist.songs.size()>(currentIndex-1)*5 + i}">
								 	<c:set value="${currentPlaylist.songs[(currentIndex-1)*5 + i]}" var="song" />	
							       <td>
								       <a class="button" href="/PlaylistMaker/removeSongFromPlaylist?playlistId=<c:out value="${currentPlaylist.id}"/>&songId=<c:out value="${song.id}"/>">
								        	remove
								        </a>
							        </td>
							    </c:when>
							 	<c:otherwise>
									<td style="width:200px;"></td>
							    </c:otherwise>        
							</c:choose>
						</c:forEach>
				    </tr>
					</table>
					
				</c:when>
					<c:otherwise>
						<p>No songs in this playlist.</p>
					</c:otherwise>
			</c:choose>

			<div class="button_div">
				<c:choose>
				<c:when test="${currentIndex > 1}">
					<a class="button" href="/PlaylistMaker/PlaylistPage?playlistId=<c:out value="${currentPlaylist.id}"/>&index=<c:out value="${currentIndex-1}"/>"> &laquo; previuous... </a>
				</c:when>
				<c:otherwise>
					<h2 style="width:120px;"></h2>
				</c:otherwise>
				</c:choose>
						
				<b>Page: <c:out value="${currentIndex}"/>/<c:out value="${Integer(Math.ceil(currentPlaylist.songs.size()/5))}"/></b>
				
				<c:choose>
				<c:when test="${currentPlaylist.songs.size()> 5*currentIndex}">
					<a class="button" href="/PlaylistMaker/PlaylistPage?playlistId=<c:out value="${currentPlaylist.id}"/>&index=<c:out value="${currentIndex+1}"/>"> next... &raquo; </a>
				</c:when>
				<c:otherwise>
					<h2 style="width:120px;"></h2>
				</c:otherwise>
				</c:choose>
			</div>
		</div>
	
		<div class="list_div">
			<h2> Add song:</h2>
			
			<c:choose>
				<c:when test="${currentSongs.size()>0}">
				<table>
					<tr>
						<td><b>Title</b></td>
						<td><b>Artist</b></td>
						<td><b>Album</b></td>
						<td><b>Year</b></td>
						<td><b>Genre</b></td>
						<td><b>Image</b></td>
						<td></td>
					</tr>
					
					
					<c:forEach var="s" items="${currentSongs}">
						<tr>
							<td><c:out value="${s.title}"/></td>
							<td><c:out value="${s.artist}"/></td>
							<td><c:out value="${s.album}"/></td>
							<td><c:out value="${s.year}"/></td>
							<td><c:out value="${s.genre}"/></td>
							
							<td><img width="80px" height="80px" src="data:image/jpeg;base64,${s.image}"/></td>
							
							<td><a class="button" href="/PlaylistMaker/addSongToPlaylist?playlistId=<c:out value="${currentPlaylist.id}"/>&songId=<c:out value="${s.id}"/>">
							<c:out value="add"/></a>
							</td>					
						</tr>
					</c:forEach>
					
					
					<tr>
					<c:forEach var="s" items="${currentSongs}">
						
					</c:forEach>
					</tr>
				</table>
				</c:when>
				<c:otherwise>
					<p>No Songs to display!</p>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>