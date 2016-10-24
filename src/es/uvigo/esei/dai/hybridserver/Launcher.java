package es.uvigo.esei.dai.hybridserver;

import java.net.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;

import java.io.*;

public class Launcher {
	public static void main(String[] args) throws IOException, SQLException {
		
		Map<String, String> pagesExample = new HashMap<>();
		
		pagesExample.put("1234", "Hola 1234");
		pagesExample.put("1111", "Hola 1111");
		
		HybridServer server;
		
		if (args.length < 1) {
			server = new HybridServer(pagesExample);
		} else {
			Properties properties = new Properties();
			
			File confFile = new File(args[0]);
			FileInputStream input = new FileInputStream(confFile);
			
			properties.load(input);
			server = new HybridServer(properties);
		}
		server.start();
	}
}
