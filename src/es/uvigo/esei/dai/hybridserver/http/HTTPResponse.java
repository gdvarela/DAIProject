package es.uvigo.esei.dai.hybridserver.http;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HTTPResponse {
	
	private String httpVersion;
	private HTTPResponseStatus httpStatus;
	private String content;
	
	private Map <String, String> parameters = new LinkedHashMap <> ();
	
	//No se puede cambiar el constructor porque los test están hechos para éste
	public HTTPResponse() {
		this.httpVersion = HTTPHeaders.HTTP_1_1.getHeader();
	}

	public HTTPResponseStatus getStatus() {
		return httpStatus;
	}

	public void setStatus(HTTPResponseStatus status) {
		this.httpStatus = status;
	}

	public String getVersion() {
		return httpVersion;
	}

	public void setVersion(String version) {
		this.httpVersion = version;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public String putParameter(String name, String value) {
		return parameters.put(name, value);
	}

	public boolean containsParameter(String name) {
		return parameters.containsKey(name);
	}

	public String removeParameter(String name) {
		return parameters.remove(name);
	}

	//Elimina todas las entradas del mapa

	public void clearParameters() {
		
		for (Entry<String, String> aux : parameters.entrySet()){
			parameters.remove(aux.getKey());
		}
		
	}
	
	//Devuelve una lista del tipo "Clave1 - Valor1, Clave2 - Valor2"

	public List<String> listParameters() {
		List <String> toRet = new LinkedList<>();
		
		for (Entry<String, String> aux : parameters.entrySet()){
			toRet.add(aux.getKey() + " - " + aux.getValue());
		}
		
		return toRet;
	}
	
	//Escribe en el writer la version, el estado y el contenido

	public void print(Writer writer) throws IOException {
		
		final StringBuilder sb = new StringBuilder(this.getVersion()).append(" ")
			.append(this.getStatus().getCode())
				.append(" ").append(this.getStatus().getStatus()).append("\r\n");
				
		for (Map.Entry<String, String> param : this.getParameters().entrySet()) {
			sb.append(param.getKey()).append(": ").append(param.getValue()).append("\r\n");
		}
		
		if(this.getContent() != null) {
			sb.append("Content-Length: " + this.getContent().length()).append("\r\n\r\n");
			sb.append(this.getContent());
		} else {
			sb.append("\r\n");	
		}
		writer.write(sb.toString());
		writer.flush();
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
