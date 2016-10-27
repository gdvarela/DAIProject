package es.uvigo.esei.dai.hybridserver.http;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class HtmlMapDAO implements HtmlDAO {

	private Map<String, String> htmlMap;
	
	public HtmlMapDAO(Map<String, String> map) {
		this.htmlMap = map;
	}
	
	@Override
	public String getHTML(String uuid) {
		return htmlMap.get(uuid);
	}
	
	@Override
	public String setHTML(String content) {
		
		UUID randomUuid = UUID.randomUUID();
		String uuid = randomUuid.toString();
		
		htmlMap.put(uuid, content);
		return uuid;
	}
	
	@Override
	public boolean hasUuid(String uuid) {
		
		return htmlMap.containsKey(uuid);
	}
	
	@Override
	public String deleteHTML(String uuid) {
		
		return htmlMap.remove(uuid);
	}
	
	@Override
	public List<String> list() {
		List <String> toRet = new LinkedList<>();
		
		for (Entry<String, String> aux : htmlMap.entrySet()){
			toRet.add(aux.getKey() + " - " + aux.getValue());
		}
		
		return toRet;
	}
}
