package servlet;

import java.io.IOException;
import java.io.InputStream;
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
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Part;

import bean.User;
import DAO.SongDAO;

/**
 * Servlet implementation class Login
 */
@WebServlet("/uploadSong")
@MultipartConfig
public class UploadSong extends HttpServlet 
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
		String title = request.getParameter("title");
		String artist = request.getParameter("artist");
		String album = request.getParameter("album");
		String genre = request.getParameter("genre");
		int year = Integer.parseInt(request.getParameter("year"));
		int userId = ((User) request.getSession().getAttribute("currentUser")).getId();
		Part filePart = request.getPart("audio");

		InputStream audioStream = null;
		String mimeType = null;
		if(filePart != null)
		{
			audioStream = filePart.getInputStream();
			String filename = filePart.getSubmittedFileName();
			mimeType = getServletContext().getMimeType(filename);	
		}
		
		if (title == null || artist == null || album == null || genre == null || (audioStream.available()==0) || !mimeType.startsWith("audio/")) 
		{
			response.sendError(505, "Parameters incomplete");
			return;
		}
	    
		SongDAO sDAO = new SongDAO(connection);
		
		try 
		{
			sDAO.uploadSong(title, artist, album, genre, year, audioStream, userId);
			response.sendRedirect(getServletContext().getContextPath() + "/upload_success.html");
		} 
		catch (SQLException e) 
		{
			response.sendRedirect(getServletContext().getContextPath() + "/upload_fail.html");
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
