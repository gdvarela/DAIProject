package es.uvigo.esei.dai.hybridserver;

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

import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.DBDAO;
import es.uvigo.esei.dai.hybridserver.http.HtmlDBDAO;
import es.uvigo.esei.dai.hybridserver.http.DBDAOFactory;

public class HybridServer {
	
	private int SERVICE_PORT = 8000;
	private int NUM_CLIENTS = 50;
	private String DB_USER;
	private String DB_PASS;
	private String DB_URL;
	
	private Thread serverThread;
	private boolean stop;
	
	private DBDAOFactory dbFactory;

	public HybridServer() {
	}
	
	public HybridServer(Properties properties) throws SQLException {
		SERVICE_PORT = Integer.parseInt(properties.getProperty("port"));
		NUM_CLIENTS = Integer.parseInt(properties.getProperty("numClients"));
		DB_USER = properties.getProperty("db.user");
		DB_PASS = properties.getProperty("db.password");
		DB_URL = properties.getProperty("db.url");	
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
}
