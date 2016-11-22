package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequestMethod;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.DBDAO;
import es.uvigo.esei.dai.hybridserver.http.DBDAOFactory;
import es.uvigo.esei.dai.hybridserver.http.HtmlDBDAO;
import es.uvigo.esei.dai.hybridserver.http.XmlDBDAO;
import es.uvigo.esei.dai.hybridserver.http.XsdDBDAO;
import es.uvigo.esei.dai.hybridserver.http.XsltDBDAO;

public class ServiceThread implements Runnable{

	private Socket socket;
	private String welocomePage = "<b> Hybrid Server </b><br>Guillermo Davila Varela<br>Samuel Ramilo Conde<br><a href=/html> Listado Archivos </a> <form action=html method=POST> <textarea name=html></textarea><button type=”submit”>Submit</button></form>";

	private HTTPResponse httpResponse = new HTTPResponse();
	private DBDAOFactory dbFactory = new DBDAOFactory();
	
	public ServiceThread(Socket socket, DBDAOFactory dbFactory) {
		this.socket = socket;
		this.dbFactory = dbFactory;
	}
	
	@Override
	public void run() {
		try {
			
			HTTPRequest httpRequest = new HTTPRequest(new InputStreamReader(socket.getInputStream()));
		 
			httpResponse.setVersion(httpRequest.getHttpVersion());
			httpResponse.setStatus(HTTPResponseStatus.S200);
			
			Connection dbConnection = dbFactory.create();
			
			String uuid = httpRequest.getResourceParameters().get("uuid");
			
			
			switch (httpRequest.getResourceName()) {
				case "html": case "xsd": case "xml": case "xslt":
					manageResource(httpRequest, dbConnection, uuid, httpRequest.getResourceName());
					break;
				case "":
					httpResponse.setContent(welocomePage);
					break;
				default:
					httpResponse.setStatus(HTTPResponseStatus.S400);
					break;
			}
			
	
		} catch (IOException | HTTPParseException | SQLException e) {
			if(e.getMessage().equals("Delete invalid uuid")) {
				httpResponse.setStatus(HTTPResponseStatus.S404);
			} else if (e.getMessage().equals("Xsd null")){
				httpResponse.setStatus(HTTPResponseStatus.S400);
			} else if (e.getMessage().equals("xsd not found")) {
				httpResponse.setStatus(HTTPResponseStatus.S404);
			} else {
				e.printStackTrace();
				httpResponse.setStatus(HTTPResponseStatus.S500);
			}
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

	private void manageResource(HTTPRequest httpRequest, Connection dbConnection, String uuid, String resource) throws SQLException {
		
		DBDAO dbDAO = null;
		
		switch (resource) {
		case "html": 
			dbDAO = new HtmlDBDAO(dbConnection);
			httpResponse.putParameter("Content-Type", "text/html");
			break;
		case "xsd":
			dbDAO = new XsdDBDAO(dbConnection);
			httpResponse.putParameter("Content-Type", "application/xml");
			break;
		case "xml":
			dbDAO = new XmlDBDAO(dbConnection);
			httpResponse.putParameter("Content-Type", "application/xml");
			break;
		case "xslt":
			dbDAO = new XsltDBDAO(dbConnection);
			httpResponse.putParameter("Content-Type", "application/xml");
			break;
		}
		
		switch (httpRequest.getMethod()) {
		case POST:
			if (httpRequest.getResourceParameters().containsKey(resource)) {
				uuid = dbDAO.setContent(httpRequest.getResourceParameters().get(resource), httpRequest.getResourceParameters().get("xsd"));
				httpResponse.setContent("<a href=\"" + resource + "?uuid=" + uuid + "\">" + uuid + "</a>");
			} else {
				httpResponse.setStatus(HTTPResponseStatus.S400);
			}
			break;
		
		case GET:
			if (httpRequest.getResourceParameters().isEmpty()) {
				
				final StringBuilder sb = new StringBuilder();
					sb.append("<b>Lista Archivos</b><br>");
				for(String s: dbDAO.list()) {
					sb.append("<a href=http://localhost:" + socket.getLocalPort() + "/html?uuid=" + s + "> " + s +" </a>");
					sb.append("<br>");
				}
				httpResponse.setContent(sb.toString());
			} else {
				
				if (dbDAO.hasUuid(uuid)) {
					httpResponse.setContent(dbDAO.getContent(uuid));
				} else {
					httpResponse.setStatus(HTTPResponseStatus.S404);
				}
			}
			break;

		case DELETE:
			dbDAO.deleteContent(uuid);
				httpResponse.setContent("<b>Se ha eliminado la pagina</b>");
			break;
			
		default:
			break;
		}
		httpResponse.putParameter("Content-Language", "en");
	}
}
