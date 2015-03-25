package ua.koxa.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Task {

	private static final ConcurrentHashMap<String, AtomicLong> visites = new ConcurrentHashMap<String, AtomicLong>();
	private final Socket connection;
	
	public Task(final Socket connection) {
		this.connection = connection;
	}
	
	public void processRequest() {
		Request request = handleRequest(connection);
		String host = request.getParameter("Host");
		AtomicLong value = visites.get(host);
	    if (value == null) {
	        value = visites.putIfAbsent(host, new AtomicLong(1));
	    }
	    if (value != null) {
	        value.incrementAndGet();
	    }
	    try {
			this.connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Request handleRequest(Socket connection) {
		try (InputStream in = connection.getInputStream();
			OutputStream out = connection.getOutputStream()) {
			String requestString = convertStreamToString(in);
			//System.out.println(requestString);
			writeAnswear(out);
			return new Request(requestString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String convertStreamToString(InputStream in) {
		StringBuilder sb = new StringBuilder();
		InputStreamReader is = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(is);
		
		try {
			String read = br.readLine();
			while (read != null && !read.isEmpty()) {
				sb.append(read + "\n");
				read = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	private void writeAnswear(OutputStream out) throws IOException {
		out.write(new Date().toString().getBytes());
	}
}
