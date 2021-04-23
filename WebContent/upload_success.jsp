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

	<h1 class="title">Playlist Maker</h1>
	
	<div class="list_div">
		<h2 style="color:green;">Song uploaded</h2>
		
		<jsp:include page="song_info.jsp"/>
	</div>
	
	<div class="button_div">
	<a class="button" href="/PlaylistMaker/HomePage"> back to Home Page </a>
	</div>
	
</div>

</body>

</html>