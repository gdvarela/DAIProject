package es.uvigo.esei.dai.hybridserver.http;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class HTTPRequest {
	
	private HTTPRequestMethod method;
	private String httpVersion;
	private Map<String, String> headerParameters = new HashMap<String, String>();
	
	public HTTPRequest(Reader reader) throws IOException, HTTPParseException {
		
		 BufferedReader bufferedReader = new BufferedReader(reader);
	        
		 StringBuilder request = new StringBuilder();
		 String line;	
		 
		 line = bufferedReader.readLine();
		 
		 Pattern pattern = Pattern.compile("([A-Z]+) / (HTTP/[0-9.]+)");
		 Matcher matcher = pattern.matcher(line);
		 matcher.find();
		 
		 this.method = HTTPRequestMethod.valueOf(matcher.group(1));
		 this.httpVersion = matcher.group(2);
		 
		 pattern = Pattern.compile("([A-Za-z-/]+): (.+)");
		 
		 while (bufferedReader.ready()) {
			 line = bufferedReader.readLine();
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
		return null;
	}

	public String[] getResourcePath() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getResourceName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, String> getResourceParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHttpVersion() {
		// TODO Auto-generated method stub
		return null;
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
