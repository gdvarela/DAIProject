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
	
	private static Properties DEFAULT_PROPERTIES = new Properties();
	
	public static void main(String[] args) throws IOException, SQLException {
		
		HybridServer server;
		
		if (args.length < 1) {
			setDefaultProperties();
			server = new HybridServer(DEFAULT_PROPERTIES);
		} else {
			Properties properties = new Properties();
			
			File confFile = new File(args[0]);
			FileInputStream input = new FileInputStream(confFile);
			
			properties.load(input);
			server = new HybridServer(properties);
		}
		server.start();
	}
	
	private static void setDefaultProperties() {
		
		DEFAULT_PROPERTIES.setProperty("numClients", "50");
		DEFAULT_PROPERTIES.setProperty("port", "8888");
		DEFAULT_PROPERTIES.setProperty("db.url", "jdbc:mysql://localhost:3306/hstestdb");
		DEFAULT_PROPERTIES.setProperty("db.user", "hsdb");
		DEFAULT_PROPERTIES.setProperty("db.password", "hsdbpass");
	}
}
