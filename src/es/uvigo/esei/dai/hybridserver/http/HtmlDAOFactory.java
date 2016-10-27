package es.uvigo.esei.dai.hybridserver.http;

import java.sql.SQLException;

public interface HtmlDAOFactory {
	
	public HtmlDAO create() throws SQLException;

}
