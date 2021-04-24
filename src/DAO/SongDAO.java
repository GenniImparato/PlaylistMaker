package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.io.InputStream;

import bean.Song;

public class SongDAO 
{
	private Connection con;
	boolean ignoreImages;
	boolean ignoreAudios;

	public SongDAO(Connection connection)
	{
		this.con = connection;
		this.ignoreAudios = false;
		this.ignoreImages = false;
	}
	
	public SongDAO(Connection connection, boolean ignoreImages, boolean ignoreAudios)
	{
		this.con = connection;
		this.ignoreAudios = ignoreAudios;
		this.ignoreImages = ignoreImages;
	}

	public Song getSongById(int songId) throws SQLException
	{
		Song s = null;
		String query = null;
		
		if(!ignoreAudios && !ignoreImages)
			query = "SELECT * FROM song WHERE id = ?";
		else if(ignoreAudios  && !ignoreImages)
			query = "SELECT id, user_id, title, artist, album, year, genre, image FROM song WHERE id = ?";
		else if(!ignoreAudios  && ignoreImages)
			query = "SELECT id, user_id, title, artist, album, year, genre, audio FROM song WHERE id = ?";
		else if(ignoreAudios  && ignoreImages)
			query = "SELECT id, user_id, title, artist, album, year, genre FROM song WHERE id = ?";
		
		ResultSet result = null;
		PreparedStatement pstatement = null;
		try 
		{
			pstatement = con.prepareStatement(query);
			pstatement.setInt(1, songId);
			result = pstatement.executeQuery();
			if(result.next()) 
			{
				s = new Song();
				s.setId(result.getInt("id"));
				s.setUserId(result.getInt("user_id"));
				s.setTitle(result.getString("title"));
				s.setArtist(result.getString("artist"));
				s.setAlbum(result.getString("album"));
				s.setYear(result.getInt("year"));
				s.setGenre(result.getString("genre"));	
				
				if(!ignoreAudios)
					s.setAudio(result.getString("audio"));
				
				if(!ignoreImages)
					s.setImage(result.getString("image"));
			}
		} 
		catch (SQLException e) 
		{
		    e.printStackTrace();
			throw new SQLException(e);
		} 
		finally 
		{
			try 
			{
				result.close();
			} 
			catch (Exception e1) 
			{
				throw new SQLException(e1);
			}
			try 
			{
				pstatement.close();
			} 
			catch (Exception e2) 
			{
				throw new SQLException(e2);
			}
		}
		
		return s;
	}
	
	public List<Song> getSongsByUser(int userId) throws SQLException
	{
		List<Song> songs = new ArrayList<Song>();
		String query = null;
		
		if(!ignoreAudios && !ignoreImages)
			query = "SELECT * FROM song WHERE user_id = ?";
		else if(ignoreAudios  && !ignoreImages)
			query = "SELECT id, user_id, title, artist, album, year, genre, image FROM song WHERE user_id = ?";
		else if(!ignoreAudios  && ignoreImages)
			query = "SELECT id, user_id, title, artist, album, year, genre, audio FROM song WHERE user_id = ?";
		else if(ignoreAudios  && ignoreImages)
			query = "SELECT id, user_id, title, artist, album, year, genre  FROM song WHERE user_id = ?";
		
		ResultSet result = null;
		PreparedStatement pstatement = null;
		try 
		{
			pstatement = con.prepareStatement(query);
			pstatement.setInt(1, userId);
			result = pstatement.executeQuery();
			while (result.next()) 
			{
				Song s = new Song();
				s.setId(result.getInt("id"));
				s.setTitle(result.getString("title"));
				s.setArtist(result.getString("artist"));
				s.setAlbum(result.getString("album"));
				s.setYear(result.getInt("year"));
				s.setGenre(result.getString("genre"));
				s.setUserId(result.getInt("user_id"));
				
				if(!ignoreAudios)
					s.setAudio(result.getString("audio"));
				
				if(!ignoreImages)
					s.setImage(result.getString("image"));
				
				songs.add(s);
			}
		} 
		catch (SQLException e) 
		{
		    e.printStackTrace();
			throw new SQLException(e);
		} 
		finally 
		{
			try 
			{
				result.close();
			} 
			catch (Exception e1) 
			{
				throw new SQLException(e1);
			}
			try 
			{
				pstatement.close();
			} 
			catch (Exception e2) 
			{
				throw new SQLException(e2);
			}
		}
		
		return songs;
	}
	
	
	public void uploadSong(Song s) throws SQLException 
	{
		
		String query = "INSERT into song (title, artist, album, genre, year, user_id, audio, image) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstatement = null;	
		try 
		{
			
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, s.getTitle());
			pstatement.setString(2, s.getArtist());
			pstatement.setString(3, s.getAlbum());
			pstatement.setString(4, s.getGenre());
			pstatement.setInt(5, s.getYear());
			pstatement.setInt(6, s.getUserId());
			pstatement.setString(7, s.getAudio());
			pstatement.setString(8, s.getImage());
			pstatement.executeUpdate();
		} 
		catch (SQLException e) 
		{
		    e.printStackTrace();
			throw new SQLException(e);
		} 
		finally 
		{
			try 
			{
				pstatement.close();
			} 
			catch (Exception e1) {}
		}

	}
}