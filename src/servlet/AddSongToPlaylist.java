package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
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
@WebServlet("/addSongToPlaylist")
@MultipartConfig
public class AddSongToPlaylist extends HttpServlet 
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
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Incorrect or missing param values");
			return;
		}
		
		int songId = 1;
		try
		{
			songId = Integer.parseInt(request.getParameter("songId"));
		}
		catch(NumberFormatException e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Incorrect or missing param values");
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
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("You don't have access to the resource");
				return;
			}
			
			pDAO.addSongToPlaylist(playlistId, songId);
			
			response.setStatus(HttpServletResponse.SC_OK);
			return;
		} 
		catch (SQLException e) 
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Databse access failed");
			return;
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
