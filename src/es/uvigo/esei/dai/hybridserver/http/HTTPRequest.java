package es.uvigo.esei.dai.hybridserver.http;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class HTTPRequest {
	
	private HTTPRequestMethod method;
	private String httpVersion;
	private String resourceChain;
	private String resourceName; //Variable o Calcularlo a partir del resourceChain?
	private String[] resourcePath;
	private ArrayList<String> pathList = new ArrayList<String>();
	private Map<String, String> headerParameters = new HashMap<String, String>();
	private Map<String, String> resourceParameters = new HashMap<String, String>();
	private StringBuilder content = new StringBuilder();

	public HTTPRequest(Reader reader) throws IOException, HTTPParseException {
	
		
		 BufferedReader bufferedReader = new BufferedReader(reader);
	        
		 StringBuilder request = new StringBuilder();
		 String line;	
		 
		 // Se lee la primera linea
		 
		 line = bufferedReader.readLine();
		 
//		 Pattern pattern = Pattern.compile("([A-Z]+) (.*) (HTTP/[0-9.]+)");
//		 Matcher matcher = pattern.matcher(line);
//		 matcher.find();
		 
//		 pattern = Pattern.compile("/([^/].[^/?]*)\\??");
//		 matcher = pattern.matcher(resourceChain);
//		 
//		 while (matcher.find()) {
//			 pathList.add(matcher.group(1));
//		 }
		 
		 
//		 resourcePath = new String[pathList.size()];
//			pathList.toArray(resourcePath);
//		 		 	
//		 pattern = Pattern.compile("([A-Za-z-/]+): (.+)");
//		 System.out.println(line);
//		 while ((line = bufferedReader.readLine()) != null) {
//			 System.out.println(line);
//			 matcher = pattern.matcher(line);
//			 if (matcher.find()) {
//				 headerParameters.put(matcher.group(1), matcher.group(2));
//			 }
//		 }		
		 
		 // Se crea una regla que va a reconocer el método, el path y la version
		 
		 Pattern pattern = Pattern.compile("([A-Z]+) (.*) (HTTP/[0-9.]+)");
		 Matcher matcher = pattern.matcher(line);
		 matcher.find();
		 
		 //Se asignan los valores encontrados a sus variables
		 
		 this.method = HTTPRequestMethod.valueOf(matcher.group(1));
		 this.resourceChain = matcher.group(2);
		 this.httpVersion = matcher.group(3);
		 
		 //Se crea otro patron para coger nombre y parametros del recurso a partir de resourceChain
		 
		 pattern = Pattern.compile("/([^?]*)\\??(.*)");
		 matcher = pattern.matcher(resourceChain);
		 matcher.find();
		 
		//Se asignan los valores encontrados a sus variables
		 
		 this.resourceName = matcher.group(1);
		 String parameters = matcher.group(2);
		 
		 //Se crea otro patron para coger el path a partir de resourceName
		 
		 pattern = Pattern.compile("([^/].[^/]*)");
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
		 System.out.println(line);
		 
		 //Se van añadiendo los parametros al mapa<parametro,valor> headerParameters
		 
		 while ((line = bufferedReader.readLine()) != null) {
			 System.out.println(line);
			 matcher = pattern.matcher(line);
			 if (matcher.find()) {
				 headerParameters.put(matcher.group(1), matcher.group(2));
			 }
		 }
		 
		 //Guarda el content en un string
		 
		 content = null;
		 
		 while ((line = bufferedReader.readLine()) != null) {
			 
			 content.append(line);			 			
			 
		 }
		 
		 
		 //System.out.println(this.toString());
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
		if (content == null){
			return null;
		}else{
			return content.toString();
		}
	}

	public int getContentLength() {
		// TODO Auto-generated method stub
		
		if (content == null){
			return 0;
		}else{
			return content.length();
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
