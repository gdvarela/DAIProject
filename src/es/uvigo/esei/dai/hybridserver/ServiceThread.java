package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.SQLException;

import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequestMethod;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.HtmlDAO;
import es.uvigo.esei.dai.hybridserver.http.HtmlDAOFactory;
import es.uvigo.esei.dai.hybridserver.http.HtmlDBDAO;
import es.uvigo.esei.dai.hybridserver.http.HtmlMapDAO;

public class ServiceThread implements Runnable{

	private Socket socket;
	private String welocomePage = "<b> Hybrid Server </b><br>Guillermo Davila Varela<br>Samuel Ramilo Conde<br><a href=/html> Listado Archivos </a> <form action=html method=POST> <textarea name=html></textarea><button type=”submit”>Submit</button></form>";

	private HTTPResponse httpResponse = new HTTPResponse();
	private HtmlDAOFactory htmlFactory;
	
	public ServiceThread(Socket socket, HtmlDAOFactory htmlFactory) {
		this.socket = socket;
		this.htmlFactory = htmlFactory;
	}
	
	@Override
	public void run() {
		try {
			
			HTTPRequest httpRequest = new HTTPRequest(new InputStreamReader(socket.getInputStream()));
		 
			httpResponse.setVersion(httpRequest.getHttpVersion());
			httpResponse.setStatus(HTTPResponseStatus.S200);
			
			String uuid = httpRequest.getResourceParameters().get("uuid");
			
			if (httpRequest.getResourceName().equals("html")) {
				
				HtmlDAO htmlDAO = htmlFactory.create();
				
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
							sb.append("<b>Lista Archivos</b>");
						for(String s: htmlDAO.list()) {
							sb.append("<a href=http://localhost:8000/html?uuid=" + s + "</a>");
							sb.append(s);
							sb.append("<br>");
						}
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
						} else {
							httpResponse.setContent("<b>Se ha eliminado la pagina</b>");
						};
					break;
					
				default:
					break;
				}
				
				httpResponse.putParameter("Content-Type", "text/html");
				httpResponse.putParameter("Content-Language", "en");
			} else if (httpRequest.getResourceName().equals("")){
				httpResponse.setContent(welocomePage);
			} else {
				httpResponse.setStatus(HTTPResponseStatus.S400);
			}
		
		} catch (IOException | HTTPParseException | SQLException e) {
			e.printStackTrace();
			httpResponse.setStatus(HTTPResponseStatus.S500);
		} finally {
			try {
				httpResponse.print(new OutputStreamWriter(socket.getOutputStream()));
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("ERROR: outputStream");
			}
		}
	}

}
