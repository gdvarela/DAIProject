package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;

public class ServiceThread implements Runnable{

	private Socket socket;
	
	public ServiceThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try (Socket socket = this.socket) {
			HTTPRequest httpRequest = new HTTPRequest(new InputStreamReader(socket.getInputStream()));
			 
			HTTPResponse httpResponse = new HTTPResponse();
			 
			httpResponse.setVersion(httpRequest.getHttpVersion());
			httpResponse.setStatus(HTTPResponseStatus.S200);
			 
			httpResponse.setContent("Hybrid Server");
			httpResponse.putParameter("Content-Type", "text/html");
			httpResponse.putParameter("Content-Language", "en");
	
			httpResponse.print(new OutputStreamWriter(socket.getOutputStream()));
		
		} catch (IOException | HTTPParseException e) {
			 e.printStackTrace(); //Cambiar Esto
		}
	}

}
