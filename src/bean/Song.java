package bean;

public class Song 
{
	private int 	id;
	private int		userid;
	private String 	title;
	private String 	artist;
	private String 	genre;
	private String 	album;
	private int 	year;
	private String	audio;
	private String  image;
	
	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}
	
	public String getTitle() 
	{
		return title;
	}
	public void setTitle(String name) 
	{
		this.title = name;
	}
	
	public String getArtist() 
	{
		return artist;
	}

	public void setArtist(String artist) 
	{
		this.artist = artist;
	}
	
	public String getGenre() 
	{
		return genre;
	}

	public void setGenre(String genre) 
	{
		this.genre = genre;
	}
	
	public String getAlbum() 
	{
		return album;
	}

	public void setAlbum(String album)
	{
		this.album = album;
	}

	public Integer getYear() 
	{
		return year;
	}

	public void setYear(Integer year) 
	{
		this.year = year;
	}

	public String getAudio() 
	{
		return audio;
	}

	public void setAudio(String audio) 
	{
		this.audio = audio;
	}

	public String getImage() 
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

	public int getUserId() 
	{
		return userid;
	}

	public void setUserId(int userid) 
	{
		this.userid = userid;
	}
}