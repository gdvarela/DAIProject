package es.uvigo.esei.dai.hybridserver.http;

import java.util.Map;

public class HtmlMapDAO implements HtmlDAO {

	private Map<String, String> htmlMap;
	
	@Override
	public String getHTML(String uuid) {
		// TODO Auto-generated method stub
		return htmlMap.get(uuid);
	}

}
