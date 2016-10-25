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
import es.uvigo.esei.dai.hybridserver.http.HtmlDBDAO;
import es.uvigo.esei.dai.hybridserver.http.HtmlMapDAO;

public class ServiceThread implements Runnable{

	private Socket socket;
	private String welocomePage = "<b>Hybrid Server</b><br>Guillermo Davila Varela<br>Samuel Ramilo Conde";
	private String DB_URL;
	private String DB_PASS;
	private String DB_USER;
	private HTTPResponse httpResponse = new HTTPResponse();
	
	public ServiceThread(Socket socket, String DB_URL, String DB_USER, String  DB_PASS) {
		this.socket = socket;
		this.DB_PASS = DB_PASS;
		this.DB_URL = DB_URL;
		this.DB_USER = DB_USER;
	}
	
	@Override
	public void run() {
		try {
			
			HTTPRequest httpRequest = new HTTPRequest(new InputStreamReader(socket.getInputStream()));
		 
			httpResponse.setVersion(httpRequest.getHttpVersion());
			httpResponse.setStatus(HTTPResponseStatus.S200);
			String uuid = httpRequest.getResourceParameters().get("uuid");
			
			HtmlDAO htmlDAO  = new HtmlDBDAO(DB_URL, DB_USER, DB_PASS);
			
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
