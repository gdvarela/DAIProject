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
			File confFile = new File(args[0]);
			FileInputStream input = new FileInputStream(confFile);
			
			if(args[0].contains(".properties")) {
				Properties properties = new Properties();	
				properties.load(input);
				server = new HybridServer(properties);
			} else {
//				int tamanoFichero = (int) confFile.length();
//				byte[] buffer = new byte[tamanoFichero];
//				
//				input.read(buffer, 0, tamanoFichero);
//				
//				server = new HybridServer(buffer);
				server = new HybridServer(confFile);
			}
			input.close();
		}
		server.start();
	}
	
	private static void setDefaultProperties() {
		
		DEFAULT_PROPERTIES.setProperty("numClients", "50");
		DEFAULT_PROPERTIES.setProperty("port", "8888");
		DEFAULT_PROPERTIES.setProperty("db.url", "jdbc:mysql://localhost:3306/hstestdb");
		DEFAULT_PROPERTIES.setProperty("db.user", "dai");
		DEFAULT_PROPERTIES.setProperty("db.password", "daipassword");
	}
}
