<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>

<head>
	<meta charset="ISO-8859-1">
	<title>Playlist Maker</title>
	<link rel="stylesheet" type="text/css" href="/PlaylistMaker/css/style.css">
</head>



<body>
<div class="main_div">

	<div class="button_div">
		<h1 class="title">Playlist Maker</h1>
		<a class="button" href="/PlaylistMaker/PlaylistPage?playlistId=<c:out value="${currentPlaylist.id}"/>"> back to playlist </a>
	</div>
	
	
	<div class="list_div">
		<h2>Song info</h2>
		
		<jsp:include page="song_info.jsp"/>
	</div>
	
</div>

</body>

</html>