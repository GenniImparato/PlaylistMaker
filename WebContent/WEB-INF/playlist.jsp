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
	<div class="main_div" style="max-width: 90%">
		<h1 class="title">Playlist Maker</h1>
		<h2>Playlist:  
			<span style='color:orange'>
				<c:out value="${currentPlaylist.name}"></c:out>
			</span>
		</h2>
		
		<div class="button_div">
			<a class="button" href="/PlaylistMaker/logout"> Log out </a>
			<a class="button" href="/PlaylistMaker/HomePage"> back to Home Page </a>
		</div>
		
		<p></p>
		
		<div class="list_div">
			<h2>Songs:</h2>
			
			<c:choose>
				<c:when test="${currentPlaylist.songs.size()>0}">
					<table style="width:100%">
					<tr>
					 	<c:forEach var = "i" begin = "1" end = "5">
					        <td><a class="button" href="/PlaylistMaker/SongPage?songId=<c:out value="${currentPlaylist.songs[i].id}"/>">
					        	<c:out value="${currentPlaylist.songs[i].title}"/>
					        </a></td>
					    </c:forEach>
				    </tr>
				    
				    <tr>
					 	<c:forEach var = "i" begin = "1" end = "5">
					        <td><img width="200" src="data:image;base64,${currentPlaylist.songs[i].image}"/></td>
					    </c:forEach>
				    </tr>
					</table>
					
				</c:when>
					<c:otherwise>
						<p>No songs.</p>
					</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>