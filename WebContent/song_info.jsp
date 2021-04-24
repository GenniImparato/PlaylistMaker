<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<table style="width:100%">

	<tr>
		<td><b>Title:</b></td>
		<td><c:out value="${lastSong.title}"></c:out></td>
	</tr>
	  	
	<tr>
		<td><b>Artist:</b></td>
		<td><c:out value="${lastSong.artist}"></c:out></td>
	</tr>
	  	
	<tr>
		<td><b>Album:</b></td>
		<td><c:out value="${lastSong.album}"></c:out></td>
	</tr>
	  	
	<tr>
		<td><b>Yeear:</b></td>
		<td><c:out value="${lastSong.year}"></c:out></td>
	</tr>
	  	
	<tr>
		<td><b>Genre:</b></td>
		<td><c:out value="${lastSong.genre}"></c:out></td>
	</tr>
</table>

<div class="button_div">
	<img width="500px" src="data:image/jpeg;base64,${lastSong.image}"/>
</div>
<div>
	<audio controls autoplay style="width:500px">
	  <source src="data:audio/ogg;base64,${lastSong.audio}">
	</audio>
</div>


