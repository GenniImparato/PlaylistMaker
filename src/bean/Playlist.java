package bean;

import java.util.List;

public class Playlist 
{
	private int 		id;
	private int			userId;
	private String 		name;
	private List<Song>	songs;
	
	public int getId() 
	{
		return id;
	}
	public void setId(int id) 
	{
		this.id = id;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public int getUserId() 
	{
		return userId;
	}
	public void setUserId(int userId) 
	{
		this.userId = userId;
	}
	public List<Song> getSongs() 
	{
		return songs;
	}
	public void setSongs(List<Song> songs) 
	{
		this.songs = songs;
	}
}