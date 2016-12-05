package es.uvigo.esei.dai.hybridserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import es.uvigo.esei.dai.hybridserver.http.DBDAOFactory;

public class HybridServer {
	
	private int SERVICE_PORT = 8888;
	private int NUM_CLIENTS = 50;
	private String DB_USER;
	private String DB_PASS;
	private String DB_URL;
	
	private Thread serverThread;
	private boolean stop;
	
	private DBDAOFactory dbFactory;

	public HybridServer() {
	}
	
	public HybridServer(Properties properties) {
		SERVICE_PORT = Integer.parseInt(properties.getProperty("port"));
		NUM_CLIENTS = Integer.parseInt(properties.getProperty("numClients"));
		DB_USER = properties.getProperty("db.user");
		DB_PASS = properties.getProperty("db.password");
		DB_URL = properties.getProperty("db.url");
		dbFactory = new DBDAOFactory(DB_URL, DB_USER, DB_PASS);
	}
	
	public HybridServer(File confFile) {
		
		try {
			Document conf = loadAndValidateWithExternalXSD(confFile);
			SERVICE_PORT = Integer.parseInt(conf.getElementsByTagName("http").item(0).getTextContent()); 
			NUM_CLIENTS = Integer.parseInt(conf.getElementsByTagName("numClients").item(0).getTextContent());
			DB_USER = conf.getElementsByTagName("user").item(0).getTextContent();
			DB_PASS = conf.getElementsByTagName("password").item(0).getTextContent();
			DB_URL = conf.getElementsByTagName("url").item(0).getTextContent();
		} catch (Exception e) {
			System.out.println("Exception: "+e.getMessage());
            System.exit(-1); 
		}
	
		dbFactory = new DBDAOFactory(DB_URL, DB_USER, DB_PASS);
	}

	public int getPort() {
		return SERVICE_PORT;
	}
	
	public void start() {
		this.serverThread = new Thread() {
			@Override
			public void run() {
				try (final ServerSocket serverSocket = new ServerSocket(SERVICE_PORT)) {
					 
					ExecutorService threadPool = Executors.newFixedThreadPool(NUM_CLIENTS);
					while (true) {
						Socket clientSocket = serverSocket.accept();
						
						if (stop) break;
						threadPool.execute(new ServiceThread(clientSocket, dbFactory));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		this.stop = false;
		this.serverThread.start();
	}
	
	public void stop() {
		this.stop = true;
		
		try (Socket socket = new Socket("localhost", SERVICE_PORT)) {
			// Esta conexión se hace, simplemente, para "despertar" el hilo servidor
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		try {
			this.serverThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		
		this.serverThread = null;
	}
	 
	 public static Document loadAndValidateWithExternalXSD(File confFile) throws ParserConfigurationException, SAXException, IOException {
			 SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			 Schema schema = schemaFactory.newSchema(new File("validationConf.xsd"));

			 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			 factory.setValidating(false);
			 factory.setNamespaceAware(true);
			 factory.setSchema(schema);
			 
			 DocumentBuilder builder = factory.newDocumentBuilder();
			 return builder.parse(confFile);
	}
}
