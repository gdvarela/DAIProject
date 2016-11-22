package es.uvigo.esei.dai.hybridserver.http;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DBDAO {

	public String getContent(String uuid) throws SQLException;
	public String setContent(String content, String xsd) throws SQLException;
	public boolean hasUuid(String uuid) throws SQLException;
	public void deleteContent(String uuid) throws SQLException;
	
	public List<String> list() throws SQLException;
}
