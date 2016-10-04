package es.uvigo.esei.dai.hybridserver;

import java.net.*;

import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;

import java.io.*;

public class Launcher {
	public static void main(String[] args) {
		
		try (ServerSocket serverSocket = new ServerSocket(8080)) {
			
			 while (true) {
				 try (Socket clientSocket = serverSocket.accept()) {
							 
					 HTTPRequest httpRequest = new HTTPRequest(new InputStreamReader(clientSocket.getInputStream()));
					 
					 HTTPResponse httpResponse = new HTTPResponse();
					 httpResponse.setVersion(httpRequest.getHttpVersion());
					 httpResponse.setStatus(HTTPResponseStatus.S200);
					 httpResponse.setContent(httpRequest.getContent());
					 httpResponse.putParameter("Content-Type", httpRequest.getHeaderParameters().get("Content-Type"));
					 httpResponse.putParameter("Content-Type", httpRequest.getHeaderParameters().get("Content-Type"));
					 
					 
					 httpResponse.putParameter("Content-Type", httpRequest.getHeaderParameters().get("Content-Type"));
					 
					 httpResponse.print(new OutputStreamWriter(clientSocket.getOutputStream()));
				 } 
			 }
		 } catch (IOException e) {
			 System.err.println("Server socket could not be created");
		 } catch (HTTPParseException e) {
				e.printStackTrace();
		}
	}
}
