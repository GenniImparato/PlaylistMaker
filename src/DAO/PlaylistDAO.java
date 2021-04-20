package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Playlist;

public class PlaylistDAO 
{
	private Connection con;

	public PlaylistDAO(Connection connection)
	{
		this.con = connection;
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