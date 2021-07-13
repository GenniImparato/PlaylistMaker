package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.PlaylistDAO;
import DAO.SongDAO;
import bean.Playlist;
import bean.Song;
import bean.User;

/**
 * Servlet implementation class HomePage
 */
@WebServlet("/PlaylistPage")
public class PlaylistPage extends HttpServlet 
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
			if (session == null || session.getAttribute("currentUser") == null) 
			{
				String path = getServletContext().getContextPath() + "/login.html";
				response.sendRedirect(path);
				return;
			}
			
			int userId = ((User) session.getAttribute("currentUser")).getId();
			
			Integer playlistId = null;
			Integer index = null;
			
			try
			{
				playlistId = Integer.parseInt(request.getParameter("playlistId"));
			}
			catch(NumberFormatException e)
			{
				response.sendError(500, e.getMessage());
			}
			
			try
			{
				index = Integer.parseInt(request.getParameter("index"));
			}
			catch(NumberFormatException e)
			{
				index = 1;
			}
			
			try 
			{
				PlaylistDAO pDAO = new PlaylistDAO(connection, false, true);
				Playlist pl = pDAO.getPlaylistById(playlistId);
				SimpleDateFormat frm = new SimpleDateFormat("dd-MM-yyyy");
				
				if(pl.getUserId() != userId)
				{
					response.sendError(500, "Not authorized to see this playlist!");
					return;
				}
				
				session.setAttribute("currentPlaylistDate", frm.format(pl.getDate()));
				
				
				
				SongDAO sDAO = new SongDAO(connection, false, true);
				List<Song> songs = sDAO.getSongsByUser(userId);
				//check index
				if(index < 1 || index > pl.getSongs().size()/5 + 1)
					index = 1;
				
				//remove songs already in playlist
				List<Song> songsFiltered = new ArrayList<Song>();session.setAttribute("currentPlaylist", pl);
				
				for(int i=0; i<songs.size(); i++)
				{
					boolean found = false;
					for(int j=0; j<pl.getSongs().size() && !found; j++)
					{
						if(pl.getSongs().get(j).getId() == songs.get(i).getId())
							found = true;
					}
					
					if(!found)
					{
						songsFiltered.add(songs.get(i));
					}
				}
									
				
				session.setAttribute("currentPlaylist", pl);
				session.setAttribute("currentSongs", songsFiltered);
				session.setAttribute("currentIndex", index);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/playlist.jsp");
				dispatcher.forward(request, response);
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

