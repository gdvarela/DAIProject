package es.uvigo.esei.dai.hybridserver.http;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
	public String getHTML(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setHTML(String content) throws SQLException {
		// TODO Auto-generated method stub
		UUID randomUuid = UUID.randomUUID();
		String uuid = randomUuid.toString();
		
		PreparedStatement statement = connection.prepareStatement("INSERT INTO HTML (uuid, content) " + "VALUES (?,?)");
		statement.setString(1, uuid);
		statement.setString(2, content);

		statement.executeUpdate();

		return uuid;
	}

	@Override
	public boolean hasUuid(String uuid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String deleteHTML(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> list() {
		// TODO Auto-generated method stub
		return null;
	}

}
