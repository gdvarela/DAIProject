package es.uvigo.esei.dai.hybridserver.http;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBDAOFactory {

	private String url;
	private String user;
	private String pass;
	
	public DBDAOFactory() {
	}
	
	public DBDAOFactory (String url, String user, String pass) {
		this.url = url;
		this.user = user;
		this.pass = pass;
	}
	
	public Connection create() throws SQLException {
		return DriverManager.getConnection(url, user, pass);
	}

}
