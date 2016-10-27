package es.uvigo.esei.dai.hybridserver.http;

import java.sql.SQLException;

public class HtmlDBDAOFactory implements HtmlDAOFactory {

	private String url;
	private String user;
	private String pass;
	
	public HtmlDBDAOFactory (String url, String user, String pass) throws SQLException {
		this.url = url;
		this.user = user;
		this.pass = pass;
	}
	
	@Override
	public HtmlDAO create() throws SQLException {
		return new HtmlDBDAO(url, user, pass);
	}

}
