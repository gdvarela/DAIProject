package es.uvigo.esei.dai.hybridserver.http;

import java.util.List;
import java.util.Map;

public interface HtmlDAO {

	public void setPages(Map<String, String> map);
	public String getHTML(String uuid);
	public String setHTML(String content);
	public boolean hasUuid(String uuid);
	public String deleteHTML(String uuid);
	
	public List<String> list();
}
