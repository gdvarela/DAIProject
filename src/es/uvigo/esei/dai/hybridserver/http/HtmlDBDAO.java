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

public class HtmlDBDAO implements DBDAO {

	private Connection connection;
	
	public HtmlDBDAO(Connection connection) throws SQLException {
		this.connection = connection;
	}

	@Override
	public String getHTML(String uuid) throws SQLException {		
		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM HTML WHERE uuid = ?")) {
			statement.setString(1, uuid);
			ResultSet result = statement.executeQuery();
			
			result.next();
			return result.getString("content");
		}
	}

	@Override
	public String setHTML(String content) throws SQLException {
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
		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM HTML WHERE uuid = ?")) {
			statement.setString(1, uuid);
			ResultSet result = statement.executeQuery();
			
			if (result.first()) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public void deleteHTML(String uuid) throws SQLException {
		String toRet;
		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM HTML WHERE uuid = ?")) {
			statement.setString(1, uuid);
			ResultSet result = statement.executeQuery();
			
			if (!result.first()) {
				throw new SQLException("Delete invalid uuid");
			}
		}
		
		try (PreparedStatement statement = connection.prepareStatement("DELETE FROM HTML WHERE uuid = ?")) {
			statement.setString(1, uuid);
			int result = statement.executeUpdate();
			
			if (result != 1) {
				throw new SQLException("Unable to delete");
			}
		}
	}

	@Override
	public List<String> list() throws SQLException {
		List<String> toRet = new LinkedList<>();
		
		try (Statement statement = connection.createStatement()) {
			try (ResultSet result = statement.executeQuery("SELECT * FROM HTML")) {
				while (result.next()) {
					toRet.add(result.getString("uuid"));
				}
			}
		}

		return toRet;
	}

}
