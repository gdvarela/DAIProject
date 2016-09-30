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

	public HTTPRequest(Reader reader) throws IOException, HTTPParseException {
		
		 BufferedReader bufferedReader = new BufferedReader(reader);
	        
		 StringBuilder request = new StringBuilder();
		 String line;	
		 
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
		 
		 Pattern pattern = Pattern.compile("([A-Z]+) (.*) (HTTP/[0-9.]+)");
		 Matcher matcher = pattern.matcher(line);
		 matcher.find();
		 
		 this.method = HTTPRequestMethod.valueOf(matcher.group(1));
		 this.resourceChain = matcher.group(2);
		 this.httpVersion = matcher.group(3);
		 
		 pattern = Pattern.compile("/([^?]*)\\??(.*)");
		 matcher = pattern.matcher(resourceChain);
		 matcher.find();
		 
		 this.resourceName = matcher.group(1);
		 String parameters = matcher.group(2);
		 
		 pattern = Pattern.compile("([^/].[^/]*)");
		 matcher = pattern.matcher(resourceName);
		 
		 while (matcher.find()) {
			 pathList.add(matcher.group(1));
		 }
		 resourcePath = new String[pathList.size()];
		 pathList.toArray(resourcePath);
		 
		 pattern = Pattern.compile("([^&].[^&]*)=([^&].[^&]*)");
		 matcher = pattern.matcher(parameters);
		 
		 while (matcher.find()) {
			 resourceParameters.put(matcher.group(1), matcher.group(2));
		 }
		 
		 pattern = Pattern.compile("([A-Za-z-/]+): (.+)");
		 System.out.println(line);
		 while ((line = bufferedReader.readLine()) != null) {
			 System.out.println(line);
			 matcher = pattern.matcher(line);
			 if (matcher.find()) {
				 headerParameters.put(matcher.group(1), matcher.group(2));
			 }
		 }
		 System.out.println(this.toString());
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
		return null;
	}

	public int getContentLength() {
		// TODO Auto-generated method stub
		return -1;
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
