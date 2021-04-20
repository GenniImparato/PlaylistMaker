package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import bean.User;

public class UserDAO 
{
	private Connection con;

	public UserDAO(Connection connection) 
	{
		this.con = connection;
	}

	public User checkUser(String username, String password) throws SQLException 
	{
		User user = null;
		String query = "SELECT * FROM user WHERE username = ? and password = ?";
		ResultSet result = null;
		PreparedStatement pstatement = null;
		
		try 
		{
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, username);
			pstatement.setString(2, password);
			result = pstatement.executeQuery();
			while (result.next()) 
			{
				user = new User();
				user.setUsername(result.getString("username"));
				user.setId(result.getInt("id"));
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
		
		return user;
	}
}

