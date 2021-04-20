package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.PlaylistDAO;
import bean.Playlist;
import bean.User;

/**
 * Servlet implementation class HomePage
 */
@WebServlet("/HomePage")
public class HomePage extends HttpServlet 
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
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("currentUser") == null) 
		{
			String path = getServletContext().getContextPath() + "/login.html";
			response.sendRedirect(path);
		}
		else 
		{
			PlaylistDAO pDAO = new PlaylistDAO(connection);
			List<Playlist> playlists;
			int userId = ((User) session.getAttribute("currentUser")).getId();
			try 
			{
				playlists = pDAO.getPlaylistsByUser(userId);
				session.setAttribute("playlists", playlists);
				response.sendRedirect(getServletContext().getContextPath() + "/home.jsp");
			} 
			catch (SQLException e) 
			{
				response.sendError(500, "Database access failed");
			}		
		}
	}


	public void destroy() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException sqle) {}
	}
}

