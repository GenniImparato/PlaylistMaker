package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Playlist;
import bean.Song;

public class PlaylistDAO 
{
	private Connection con;
	boolean ignoreImages;
	boolean ignoreAudios;

	public PlaylistDAO(Connection connection)
	{
		this.con = connection;
		this.ignoreAudios = false;
		this.ignoreImages = false;
	}
	
	public PlaylistDAO(Connection connection, boolean ignoreImages, boolean ignoreAudios)
	{
		this.con = connection;
		this.ignoreAudios = ignoreAudios;
		this.ignoreImages = ignoreImages;
	}
	
	public Playlist getPlaylistById(int playlistId) throws SQLException
	{
		Playlist pl = new Playlist();
		String query = "SELECT * FROM playlist WHERE id = ?";
		ResultSet result = null;
		PreparedStatement pstatement = null;
		try 
		{
			pstatement = con.prepareStatement(query);
			pstatement.setInt(1, playlistId);
			result = pstatement.executeQuery();
			if(result.next()) 
			{
				pl.setId(result.getInt("id"));
				pl.setName(result.getString("name"));
				pl.setUserId(result.getInt("user_id"));
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
		
		query = "SELECT song_id FROM song_in_playlist WHERE playlist_id = ? order by (SELECT year from song where id = song_id) DESC";
		List<Song> songs = new ArrayList<Song>();
		
		try 
		{
			pstatement = con.prepareStatement(query);
			pstatement.setInt(1, playlistId);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				SongDAO sDao = new SongDAO(con, ignoreImages, ignoreAudios);
				songs.add(sDao.getSongById(result.getInt("song_id")));
			}
			
			pl.setSongs(songs);
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
		
		return pl;
	}

	public List<Playlist> getPlaylistsByUser(int userId) throws SQLException
	{
		List<Playlist> playlists = new ArrayList<Playlist>();
		String query = "SELECT * FROM playlist WHERE user_id = ?";
		ResultSet result = null;
		PreparedStatement pstatement = null;
		try 
		{
			pstatement = con.prepareStatement(query);
			pstatement.setInt(1, userId);
			result = pstatement.executeQuery();
			while (result.next()) 
			{
				Playlist pl = new Playlist();
				pl.setId(result.getInt("id"));
				pl.setName(result.getString("name"));
				
				playlists.add(pl);
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
		
		return playlists;
	}
	
	
	public void createPlaylist(String name, int userId) throws SQLException 
	{
		String query = "INSERT into playlist (name, user_id) VALUES(?, ?)";
		PreparedStatement pstatement = null;	
		try 
		{
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, name);
			pstatement.setInt(2, userId);
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
	
	public void addSongToPlaylist(int playlistId, int songId) throws SQLException 
	{
		String query = "INSERT into song_in_playlist (playlist_id, song_id) VALUES(?, ?)";
		PreparedStatement pstatement = null;	
		try 
		{
			pstatement = con.prepareStatement(query);
			pstatement.setInt(1, playlistId);
			pstatement.setInt(2, songId);
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
	
	public void removeSongFromPlaylist(int playlistId, int songId) throws SQLException 
	{
		String query = "DELETE from song_in_playlist WHERE playlist_id = ? and song_id = ?";
		PreparedStatement pstatement = null;	
		try 
		{
			pstatement = con.prepareStatement(query);
			pstatement.setInt(1, playlistId);
			pstatement.setInt(2, songId);
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
	
	/*
	public int pinPost(int postId, int userId) throws SQLException {
		String query = "UPDATE post SET pinned = (CASE "
				+ "WHEN id = ? THEN 1 ELSE 0 END)"
				+ "WHERE userid = ?";
		PreparedStatement pstatement = null;
		int code = 0;		
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setInt(1, postId);
			pstatement.setInt(2, userId);
			code = pstatement.executeUpdate();
		} catch (SQLException e) {
		    e.printStackTrace();
			throw new SQLException(e);
		} finally {
			try {
				pstatement.close();
			} catch (Exception e1) {}
		}		
		return code;
	}
	
	public int unpinPost(int postId, int userId) throws SQLException {
		String query = "UPDATE post SET pinned = 0 WHERE id = ? AND userid = ?";
		PreparedStatement pstatement = null;
		int code = 0;		
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setInt(1, postId);
			pstatement.setInt(2, userId);
			code = pstatement.executeUpdate();
		} catch (SQLException e) {
		    e.printStackTrace();
			throw new SQLException(e);
		} finally {
			try {
				pstatement.close();
			} catch (Exception e1) {}
		}		
		return code;
	}
*/
}