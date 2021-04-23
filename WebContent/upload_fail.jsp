<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

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
		<h2 style="color:red;">Song upload failed.</h2>
		<p><c:out value="${lastMessage}"></c:out></p>
		
		<div class="button_div">
			<a class="button" href="/PlaylistMaker/HomePage"> back to Home Page </a>
		</div>
	</div>
	
</div>

</body>

</html>