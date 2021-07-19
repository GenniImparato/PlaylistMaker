package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import bean.Song;
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Song s = new Song();
		
		//check all parameters
		s.setTitle(request.getParameter("title"));
		if(s.getTitle() == null)
		{
			redirectToErrorPage(request, response, "Invalid title");
			return;
		}
		
		s.setArtist(request.getParameter("artist"));
		if(s.getArtist() == null || s.getArtist().length()==0)
			s.setArtist("Unknown artist");
			
		
		s.setAlbum(request.getParameter("album"));
		if(s.getAlbum() == null || s.getAlbum().length()==0)
			s.setAlbum("Unknown album");
		
		s.setGenre(request.getParameter("genre"));
		if(s.getGenre() == null || s.getGenre().length()==0)
			s.setGenre("Unknown genre");
		
		String yearString = request.getParameter("year");
		if(yearString == null || yearString.length()==0)
			s.setYear(0);
		else
		{
			try
			{
				s.setYear(Integer.parseInt(request.getParameter("year")));
			}
			catch(NumberFormatException e)
			{
				redirectToErrorPage(request, response, "Invalid year:\r\n" + e.getMessage());
				return;
			}
		}
		
		if(request.getSession(false).getAttribute("currentUser") != null)
			s.setUserId(((User) request.getSession(false).getAttribute("currentUser")).getId());
		else
		{
			response.sendRedirect(getServletContext().getContextPath() + "/login.html");
			return;
		}
		
		Part filePart = request.getPart("audio");
		InputStream fileStream = null;
		String mimeType = null;
		if(filePart != null)
		{
			fileStream = filePart.getInputStream();
			String filename = filePart.getSubmittedFileName();
			mimeType = getServletContext().getMimeType(filename);	
		}
		else
		{
			redirectToErrorPage(request, response, "Audio file can't be null");
			return;
		}
		
		if(fileStream.available()==0 || !mimeType.startsWith("audio/"))
		{
			redirectToErrorPage(request, response, "Invalid audio file");
			return;
		}
		
		s.setAudio(Base64.getEncoder().encodeToString(fileStream.readAllBytes()));
		
		filePart = request.getPart("image");
		if(filePart != null)
		{
			fileStream = filePart.getInputStream();
			String filename = filePart.getSubmittedFileName();
			mimeType = getServletContext().getMimeType(filename);
			
			s.setImage(Base64.getEncoder().encodeToString(fileStream.readAllBytes()));
			
			if(mimeType!=null && !mimeType.startsWith("image/"))
			{
				s.setImage(null);
				redirectToErrorPage(request, response, "Invalid image file");
				return;
			}			
		}
		else
			s.setImage(null);
		
		
		
		
		
		SongDAO sDAO = new SongDAO(connection);
		
		//writes new song to database
		try 
		{
			sDAO.uploadSong(s);
			request.getSession().setAttribute("lastSong", s);
			response.sendRedirect(getServletContext().getContextPath() + "/upload_success.jsp");
		} 
		catch (SQLException e) 
		{
			redirectToErrorPage(request, response, "Error accessing database:" + e.getMessage());
			return;
		}
	}
	
	public void redirectToErrorPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException
	{
		request.getSession().setAttribute("lastMessage", message);
		response.sendRedirect(getServletContext().getContextPath() + "/upload_fail.jsp");
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
