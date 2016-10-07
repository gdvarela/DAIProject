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
		return parameters;
	}

	public String putParameter(String name, String value) {
		// TODO Auto-generated method stub
		return parameters.put(name, value);
	}

	public boolean containsParameter(String name) {
		// TODO Auto-generated method stub
		return parameters.containsKey(name);
	}

	public String removeParameter(String name) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		List <String> toRet = new LinkedList<>();
		
		for (Entry<String, String> aux : parameters.entrySet()){
			toRet.add(aux.getKey() + " - " + aux.getValue());
		}
		
		return toRet;
	}
	
	//Escribe en el writer la version, el estado y el contenido

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
