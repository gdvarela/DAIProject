package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequestMethod;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.HtmlDAO;
import es.uvigo.esei.dai.hybridserver.http.HtmlMapDAO;

public class ServiceThread implements Runnable{

	private Socket socket;
	private HtmlDAO htmlDAO;
	
	public ServiceThread(Socket socket, HtmlDAO htmlDAO) {
		this.socket = socket;
		this.htmlDAO = htmlDAO;
	}
	
	@Override
	public void run() {
		try (Socket socket = this.socket) {
			HTTPRequest httpRequest = new HTTPRequest(new InputStreamReader(socket.getInputStream()));
			 
			HTTPResponse httpResponse = new HTTPResponse();
			 
			httpResponse.setVersion(httpRequest.getHttpVersion());
			httpResponse.setStatus(HTTPResponseStatus.S200);
			String uuid = httpRequest.getResourceParameters().get("uuid");
			
			if (httpRequest.getResourceName().equals("html")) {
				
				switch (httpRequest.getMethod()) {
				case POST:
					if (httpRequest.getResourceParameters().containsKey("html")) {
						uuid = htmlDAO.setHTML(httpRequest.getResourceParameters().get("html"));
						httpResponse.setContent("<a href=\"html?uuid=" + uuid + "\">" + uuid + "</a>");
					} else {
						httpResponse.setStatus(HTTPResponseStatus.S400);
					}
					break;
				
				case GET:
					if (httpRequest.getResourceParameters().isEmpty()) {
						
						final StringBuilder sb = new StringBuilder();
							sb.append("<html>");
						for(String s: htmlDAO.list()) {
							sb.append(s);
							sb.append("<br>");
						}
							sb.append("</html>");
						httpResponse.setContent(sb.toString());
					} else {
						
						if (htmlDAO.hasUuid(uuid)) {
							httpResponse.setContent(htmlDAO.getHTML(uuid));
						} else {
							httpResponse.setStatus(HTTPResponseStatus.S404);
						}
					}
					break;

				case DELETE:
						if (htmlDAO.deleteHTML(uuid) == null) {
							httpResponse.setStatus(HTTPResponseStatus.S404);
						};
					break;
					
				default:
					break;
				}
				
				httpResponse.putParameter("Content-Type", "text/html");
				httpResponse.putParameter("Content-Language", "en");
			} else {
				httpResponse.setStatus(HTTPResponseStatus.S400);
			}
			
			httpResponse.print(new OutputStreamWriter(socket.getOutputStream()));
		
		} catch (IOException | HTTPParseException e) {
			 e.printStackTrace(); //Cambiar Esto
		}
	}

}
