package es.uvigo.esei.dai.hybridserver.http;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DBDAO {

	public String getHTML(String uuid) throws SQLException;
	public String setHTML(String content) throws SQLException;
	public boolean hasUuid(String uuid) throws SQLException;
	public void deleteHTML(String uuid) throws SQLException;
	
	public List<String> list() throws SQLException;
}
