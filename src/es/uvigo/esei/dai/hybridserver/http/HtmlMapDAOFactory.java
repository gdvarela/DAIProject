package es.uvigo.esei.dai.hybridserver.http;

import java.sql.SQLException;
import java.util.Map;

public class HtmlMapDAOFactory implements HtmlDAOFactory{
	
	private Map<String, String> pages;
	
	public HtmlMapDAOFactory(Map<String, String> map) {
		this.pages =map;
	}

	@Override
	public HtmlDAO create() throws SQLException {
		return new HtmlMapDAO(pages);
	}

}
