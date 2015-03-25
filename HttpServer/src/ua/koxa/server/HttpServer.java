package ua.koxa.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer {
	
	public static void main(String[] args) {
		try {
			new HttpServer().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final ExecutorService executorService = new FixedThreadPoolExecutorService(10, 500);

	public void start() throws IOException {
		
		try (ServerSocket socket = new ServerSocket()) {
			socket.setReuseAddress(true);
			socket.bind(new InetSocketAddress(12345));
			
			log("server started on port " + socket.getLocalPort());
			
			while (!executorService.isShutdown()) {
				try {
					executorService.execute(new Task(socket.accept()));
					//executorService.shutdown();
					//executorService.shutdownNow();
					
				} catch (RejectedExecutionException e) {
					if (!executorService.isShutdown())
						log("task submission rejected", e);
				}
			}
		}
	}

	public void stop() {
		executorService.shutdown();
	}

	private void log(String msg) {
		Logger.getAnonymousLogger().log(Level.SEVERE, msg);
	}
	
	private void log(String msg, Exception e) {
		Logger.getAnonymousLogger().log(Level.SEVERE, msg, e);
	}
}
