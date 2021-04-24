package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import DAO.PlaylistDAO;
import DAO.SongDAO;

/**
 * Servlet implementation class Login
 */
@WebServlet("/removeSongFromPlaylist")
public class RemoveSongFromPlaylist extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public void init() throws ServletException 
	{
		try 
		{
			ServletContext context = getServletContext();
			String driver = context.getInitParameter("dbDriver");
			String url = context.getInitParameter("dbUrl");
			String user = context.getInitParameter("dbUser");
			String password = context.getInitParameter("dbPassword");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);

		} 
		catch (ClassNotFoundException e) 
		{
		    e.printStackTrace();
			throw new UnavailableException("Can't load database driver");
		} 
		catch (SQLException e) 
		{
		    e.printStackTrace();
			throw new UnavailableException("Couldn't get db connection");
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("currentUser") == null) 
		{
			String path = getServletContext().getContextPath() + "/login.html";
			response.sendRedirect(path);
		}
		
		int playlistId = 1;
		try
		{
			playlistId = Integer.parseInt(request.getParameter("playlistId"));
		}
		catch(NumberFormatException e)
		{
			response.sendError(505, e.getMessage());
			return;
		}
		
		int songId = 1;
		try
		{
			songId = Integer.parseInt(request.getParameter("songId"));
		}
		catch(NumberFormatException e)
		{
			response.sendError(505, e.getMessage());
			return;
		}

		/*if (name == null || playlistId == ) 
		{
			response.sendError(505, "Parameters incomplete");
			return;
		}*/
	    
		
		int userId = ((User) session.getAttribute("currentUser")).getId();
		
		try 
		{
			PlaylistDAO pDAO = new PlaylistDAO(connection);
			SongDAO	sDAO = new SongDAO(connection);
			
			if(sDAO.getSongById(songId).getUserId() != userId	||	pDAO.getPlaylistById(playlistId).getUserId() != userId)
			{
				response.sendError(505, "You don't have access to those resources");
				return;
			}
			
			pDAO.removeSongFromPlaylist(playlistId, songId);
			
			response.sendRedirect(getServletContext().getContextPath() + "/PlaylistPage?playlistId=" + playlistId);
		} 
		catch (SQLException e) 
		{
			response.sendError(500, "Database access failed");
		}
	}

	
	public void destroy() 
	{
		try 
		{
			if (connection != null) 
				connection.close();
		} 
		catch(SQLException sqle) 
		{}
	}

}
