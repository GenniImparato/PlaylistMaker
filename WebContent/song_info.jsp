<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<p>
<label for="title"><b>Title:</b> <c:out value="${lastSong.title}"></c:out></label>
</p>
  	
<p>
<label for="artist"><b>Artist:</b> <c:out value="${lastSong.artist}"></c:out></label>
</p>
  	
<p>
<label for="album"><b>Album:</b> <c:out value="${lastSong.album}"></c:out></label>
</p>
  	
<p>
<label for="year"><b>Year:</b> <c:out value="${lastSong.year}"></c:out></label>
</p>
  	
<p>
<label for="genre"><b>Genre:</b> <c:out value="${lastSong.genre}"></c:out></label>
</p>
	
<p>
<img width="500" src="data:image/jpeg;base64,${lastSong.image}"/>
</p>
	
<p>
<audio controls style="width:500px">
  <source src="data:audio/ogg;base64,${lastSong.audio}">
</audio>
</p>
