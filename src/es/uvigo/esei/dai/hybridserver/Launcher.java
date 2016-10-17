package es.uvigo.esei.dai.hybridserver;

import java.net.*;

import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;

import java.io.*;

public class Launcher {
	public static void main(String[] args) {
		
		
		HybridServer server = new HybridServer();
		server.start();
	}
}
