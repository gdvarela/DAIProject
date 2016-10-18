package es.uvigo.esei.dai.hybridserver;

import java.net.*;
import java.util.HashMap;
import java.util.Map;

import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;

import java.io.*;

public class Launcher {
	public static void main(String[] args) {
		
		Map<String, String> pagesExample = new HashMap<>();
		
		pagesExample.put("1234", "Hola 1234");
		pagesExample.put("1111", "Hola 1111");
		
		HybridServer server = new HybridServer(pagesExample);
		server.start();
	}
}
