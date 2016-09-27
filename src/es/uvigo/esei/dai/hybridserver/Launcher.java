package es.uvigo.esei.dai.hybridserver;

import java.net.*;

import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;

import java.io.*;

public class Launcher {
	public static void main(String[] args) {
		
		try (ServerSocket serverSocket = new ServerSocket(8080)) {
			
			 while (true) {
				 try (Socket clientSocket = serverSocket.accept()) {
							 
					 HTTPRequest httpRequest = new HTTPRequest(new InputStreamReader(clientSocket.getInputStream()));
				 } 
			 }
		 } catch (IOException e) {
			 System.err.println("Server socket could not be created");
		 } catch (HTTPParseException e) {
				e.printStackTrace();
		}
	}
}
