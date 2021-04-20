package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Part;
import java.io.InputStream;

import bean.Song;

public class SongDAO 
{
	private Connection con;

	public SongDAO(Connection connection)
	{
		this.con = connection;
	}

	public List<Song> getSongsByUser(int userId) throws SQLException
	{
		List<Song> songs = new ArrayList<Song>();
		String query = "SELECT * FROM song WHERE user_id = ?";
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
	
	
	public void uploadSong(String title, String artist, String album, String genre, int year, InputStream audioFile, int userId) throws SQLException 
	{
		
		String query = "INSERT into song (title, artist, album, genre, year, user_id, audio) VALUES(?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstatement = null;	
		try 
		{
			
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, title);
			pstatement.setString(2, artist);
			pstatement.setString(3, album);
			pstatement.setString(4, genre);
			pstatement.setInt(5, year);
			pstatement.setInt(6, userId);
			pstatement.setBlob(7, audioFile);
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