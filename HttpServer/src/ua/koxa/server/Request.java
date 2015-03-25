package ua.koxa.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Request {

	private final Map<String, String> parameters = new HashMap<>();
	private final ConcurrentHashMap<String, String> attributes = new ConcurrentHashMap<>();
	private String requestMethod;
	private String httpVersion;
	private String requestUrl; 
	
	public Request(String request) {
		String[] headers = ("Request: " + request).split("\n");
		populateHeaders(headers);
		populateAttributes(parameters.get("Request"));
	}
	
	private void populateHeaders(final String[] headers) {
		for (int i = 0; i < headers.length; i++) {
			String[] parameterKeyValue = headers[i].split(": ");
			parameters.put(parameterKeyValue[0], parameterKeyValue[1]);
		}
	}
	
	private void populateAttributes(String request) {
		String[] requestParts = request.split(" ");
		requestMethod = requestParts[0];
		httpVersion = requestParts[2];
		int attributesStartIndex = requestParts[1].indexOf("?");
		if (attributesStartIndex != -1) {
			requestUrl = requestParts[1].substring(0, attributesStartIndex);
			
			String[] attributePairs = requestParts[1].substring(attributesStartIndex + 1).split("&");
			for (String attributePair : attributePairs) {
				String[] attributeKeyValue = attributePair.split("=");
				attributes.put(attributeKeyValue[0], attributeKeyValue[1]);
			}
		}
	}
	
	public Map<String, String> getParameterMap() {
		return new HashMap<String, String>(this.parameters);
	}
	
	public Map<String, String> getAttributeMap() {
		// TODO: reimplement
		synchronized (this.attributes) {
			return new HashMap<String, String>(this.attributes);
		}
	}
	
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	public String getAttribute(String name) {
		return attributes.get(name);
	}
	
	public String addAttribute(String name, String value) {
		return attributes.put(name, value);
	}
	
	public String getRequestMethod() {
		return requestMethod;
	}
	
	public String getHttpVersion() {
		return httpVersion;
	}
}
