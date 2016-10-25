package es.uvigo.esei.dai.hybridserver.http;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HtmlDBDAO implements HtmlDAO {

	private Connection connection;
	
	public HtmlDBDAO(String url, String user, String pass) throws SQLException {
		connection = DriverManager.getConnection(url, user, pass);
	}
	
	@Override
	public void setPages(Map<String, String> map) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getHTML(String uuid) throws SQLException {
		// TODO Auto-generated method stub
		
		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM HTML WHERE uuid = ?")) {
			statement.setString(1, uuid);
			ResultSet result = statement.executeQuery();
			
			result.next();
			return result.getString("content");
		}
	}

	@Override
	public String setHTML(String content) throws SQLException {
		// TODO Auto-generated method stub
		UUID randomUuid = UUID.randomUUID();
		String uuid = randomUuid.toString();
		
		try (PreparedStatement statement = connection.prepareStatement("INSERT INTO HTML (uuid, content) " + "VALUES (?,?)")) {
			statement.setString(1, uuid);
			statement.setString(2, content);

			int result = statement.executeUpdate();
			
			if (result != 1) {
				throw new SQLException("Unable to insert new page");
			}
		}
		
		return uuid;
	}

	@Override
	public boolean hasUuid(String uuid) throws SQLException {
		// TODO Auto-generated method stub
		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM HTML WHERE uuid = ?")) {
			statement.setString(1, uuid);
			
			if (statement.execute()) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public String deleteHTML(String uuid) throws SQLException {
		// TODO Auto-generated method stub
		String toRet;
		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM HTML WHERE uuid = ?")) {
			statement.setString(1, uuid);
			ResultSet result = statement.executeQuery();
			
			result.next();
			toRet =  result.getString("content");
		}
		
		try (PreparedStatement statement = connection.prepareStatement("DELETE FROM HTML WHERE uuid = ?")) {
			statement.setString(1, uuid);
			int result = statement.executeUpdate();
			
			if (result != 1) {
				throw new SQLException("Unable to delete");
			}
		}
		
		return toRet;
	}

	@Override
	public List<String> list() throws SQLException {
		// TODO Auto-generated method stub
		List<String> toRet = new LinkedList<>();
		
		try (Statement statement = connection.createStatement()) {
			try (ResultSet result = statement.executeQuery("SELECT * FROM HTML")) {
				while (result.next()) {
					toRet.add("<a href=http://localhost:8000/html?uuid=" + result.getString("uuid") + ">" + result.getString("uuid") + "</a>");
				}
			}
		}

		return toRet;
	}

}
