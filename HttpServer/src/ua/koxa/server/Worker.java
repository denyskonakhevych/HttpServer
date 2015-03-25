package ua.koxa.server;

import java.util.concurrent.BlockingQueue;

public class Worker implements Runnable{
	
	private BlockingQueue<Task> workQueue;
	private volatile boolean isShutdown = false;
	
	public Worker(BlockingQueue<Task> workQueue) {
		this.workQueue = workQueue;
	}
	
	@Override
	public void run() {
		try {
			while (!isShutdown) {
				//System.out.println("try get");
				Task task = this.workQueue.take();
				//System.out.println("got one");
				task.processRequest();
				//System.out.println("Ok");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
		isShutdown = true;
		System.out.println("Shutdown: " + isShutdown);
	}
	
}
