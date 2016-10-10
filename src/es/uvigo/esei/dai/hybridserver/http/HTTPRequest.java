package es.uvigo.esei.dai.hybridserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPRequest {
	
	private HTTPRequestMethod method;
	private String httpVersion;
	private String resourceChain;
	private String resourceName; //Variable o Calcularlo a partir del resourceChain?
	private String[] resourcePath;
	private List<String> pathList = new ArrayList<>();
	
	//Se utilizan LinkedHashMap en lugar de HashMap para evitar un fallo en HTTPRequest.toString
	
	private Map<String, String> headerParameters = new LinkedHashMap<>();
	private Map<String, String> resourceParameters = new LinkedHashMap<>();
	private String content = null;

	//Mover todo esto del constructor a un metodo 'process'
	
	public HTTPRequest(Reader reader) throws IOException, HTTPParseException {
	
		
		 BufferedReader bufferedReader = new BufferedReader(reader);
	        
		 StringBuilder request = new StringBuilder();
		 String line;	
		 
		 // Se lee la primera linea
		 
		 line = bufferedReader.readLine();
		 
		 // Se crea una regla que va a reconocer el método, el path y la version
		 
		 Pattern pattern = Pattern.compile("(GET|POST|PUT|DELETE|HEAD|TRACE|OPTIONS|CONNECT) ");
		 Matcher matcher = pattern.matcher(line);
		 
		 try {
			 matcher.find();
			 this.method = HTTPRequestMethod.valueOf(matcher.group(1));
		 } catch (java.lang.IllegalStateException e) {
			 throw new HTTPParseException("Missing Method");
		}
		 
		 try {
			 pattern = Pattern.compile(" (.*) ");
			 matcher = pattern.matcher(line);
			  
			 matcher.find();
			 this.resourceChain = matcher.group(1);

		 } catch (java.lang.IllegalStateException e) {
			 throw new HTTPParseException("Missing resource");
		}
		 
		 try {
			 pattern = Pattern.compile(" (HTTP/[0-9.]+)");
			 matcher = pattern.matcher(line);
			 
			 matcher.find();
			 this.httpVersion = matcher.group(1);
		 } catch (java.lang.IllegalStateException e) {
			 throw new HTTPParseException("Missing version");
		}
		 
		 //Se crea otro patron para coger nombre y parametros del recurso a partir de resourceChain
		 
		 pattern = Pattern.compile("/([^?]*)\\??(.*)");
		 matcher = pattern.matcher(resourceChain);
		 matcher.find();
		 
		//Se asignan los valores encontrados a sus variables
		 
		 this.resourceName = matcher.group(1);
		 String parameters = matcher.group(2);
		 
		 //Se crea otro patron para coger el path a partir de resourceName
		 
		 pattern = Pattern.compile("([^/]+)");
		 matcher = pattern.matcher(resourceName);
		 
		 //Se crea una lista con los valores que va a tener el array del Path
		 
		 while (matcher.find()) {
			 pathList.add(matcher.group(1));
		 }
		 
		 //Se crea un array de String resourcePath con los valores guardados en la lista anterior
		 
		 resourcePath = new String[pathList.size()];
		 pathList.toArray(resourcePath);
		 
		 //Se crea un patron para los parametros del recurso de forma param1=valor1
		 
		 pattern = Pattern.compile("([^&].[^&]*)=([^&].[^&]*)");
		 matcher = pattern.matcher(parameters);
		 
		 //Se van añadiendo estos parametros al mapa <parametro,valor> resourceParameters
		 
		 while (matcher.find()) {
			 resourceParameters.put(matcher.group(1), matcher.group(2));
		 }
		 
		 //Se crea un patron para coger los parametros de la cabecera en las siguientes linea
		 
		 pattern = Pattern.compile("([A-Za-z-/]+): (.+)");
		// System.out.println(line);
		 
		 //Se van añadiendo los parametros al mapa<parametro,valor> headerParameters
		 try {
			 while (!(line = bufferedReader.readLine()).isEmpty()) {
				 //System.out.println(line);
				 matcher = pattern.matcher(line);
				 if (matcher.find()) {
					 headerParameters.put(matcher.group(1), matcher.group(2));
				 }
			 }
		} catch (Exception e) {
			 throw new HTTPParseException("Invalid header");
		}
		  
		 // Para el contenido hay que diferencial el metodo 
		 int length;
		 
		 if ((length = this.getContentLength()) != 0) {
			 
			 char[] buffer = new char[length];
			 bufferedReader.read(buffer ,0 ,length);
			 
			 content = new String (buffer);
			 String type = headerParameters.get("Content-Type");

			 if (type != null && type.startsWith("application/x-www-form-urlencoded")) {
		 		 content = URLDecoder.decode(content, "UTF-8");
			 } 
			 
			 if (this.getMethod() == HTTPRequestMethod.POST) {
				 
				 pattern = Pattern.compile("([^&].[^&]*)=([^&].[^&]*)");
				 matcher = pattern.matcher(content);
					
				 while (matcher.find()) {
						 resourceParameters.put(matcher.group(1), matcher.group(2));
				 }
			 }	
		 }
	}

	public HTTPRequestMethod getMethod() {
		// TODO Auto-generated method stub
		return method;
	}

	public String getResourceChain() {
		// TODO Auto-generated method stub
		return resourceChain;
	}

	public String[] getResourcePath() {
		// TODO Auto-generated method stub
		return resourcePath;
	}

	public String getResourceName() {
		// TODO Auto-generated method stub
		return resourceName;
	}

	public Map<String, String> getResourceParameters() {
		// TODO Auto-generated method stub
		return resourceParameters;
	}

	public String getHttpVersion() {
		// TODO Auto-generated method stub
		return httpVersion;
	}

	public Map<String, String> getHeaderParameters() {
		// TODO Auto-generated method stub
		return headerParameters;
	}

	public String getContent() {
		// TODO Auto-generated method stub
		return content;
	}

	public int getContentLength() {
		// TODO Auto-generated method stub
		if (headerParameters.containsKey("Content-Length")) {
			return Integer.parseInt(headerParameters.get("Content-Length"));
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(this.getMethod().name()).append(' ').append(this.getResourceChain())
				.append(' ').append(this.getHttpVersion()).append("\r\n");

		for (Map.Entry<String, String> param : this.getHeaderParameters().entrySet()) {
			sb.append(param.getKey()).append(": ").append(param.getValue()).append("\r\n");
		}

		if (this.getContentLength() > 0) {
			sb.append("\r\n").append(this.getContent());
		}

		return sb.toString();
	}
}
