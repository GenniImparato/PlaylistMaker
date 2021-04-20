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

import bean.User;
import DAO.PlaylistDAO;

/**
 * Servlet implementation class Login
 */
@WebServlet("/createPlaylist")
public class CreatePlaylist extends HttpServlet 
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
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String name = request.getParameter("name");

		if (name == null) 
		{
			response.sendError(505, "Parameters incomplete");
			return;
		}
	    
		PlaylistDAO pDAO = new PlaylistDAO(connection);
		int userId = ((User) request.getSession().getAttribute("currentUser")).getId();
		
		try 
		{
			pDAO.createPlaylist(name, userId);
			response.sendRedirect(getServletContext().getContextPath() + "/HomePage");
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
