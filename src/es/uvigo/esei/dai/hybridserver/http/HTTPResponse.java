package es.uvigo.esei.dai.hybridserver.http;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class HTTPResponse {
	
	private String httpVersion;
	private HTTPResponseStatus httpStatus;
	private String content;
	
	public HTTPResponse() {
	}

	public HTTPResponseStatus getStatus() {
		// TODO Auto-generated method stub
		return httpStatus;
	}

	public void setStatus(HTTPResponseStatus status) {
		this.httpStatus = status;
	}

	public String getVersion() {
		// TODO Auto-generated method stub
		return httpVersion;
	}

	public void setVersion(String version) {
		this.httpVersion = version;
	}

	public String getContent() {
		// TODO Auto-generated method stub
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, String> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	public String putParameter(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean containsParameter(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	public String removeParameter(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public void clearParameters() {
	}

	public List<String> listParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	public void print(Writer writer) throws IOException {
		
		final StringBuilder sb = new StringBuilder(this.getVersion()).append(" ").append(this.getStatus().getCode())
				.append(" ").append(this.getStatus().getStatus()).append("\r\n");
				
		if(this.getContent() != null) {
			sb.append(this.getContent());
		}
		
		sb.append("\r\n");
		
		writer.write(sb.toString());
	}

	@Override
	public String toString() {
		final StringWriter writer = new StringWriter();

		try {
			this.print(writer);
		} catch (IOException e) {
		}

		return writer.toString();
	}
}
