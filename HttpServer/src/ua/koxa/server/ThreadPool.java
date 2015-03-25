package ua.koxa.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class ThreadPool {

	private final Set<Thread> pool;
	private final BlockingQueue<Task> workQueue;
	//private final List<Thread> workerThread;
	private final Set<Worker> workers;
	
	public ThreadPool(int threadNumber, BlockingQueue<Task> workQueue) {
		this.workQueue = workQueue;
		workers = new HashSet<>(threadNumber);
		pool = new HashSet<>(threadNumber);
		for (int i = 0; i < threadNumber; i++) {
			Worker worker = new Worker(workQueue);
			workers.add(worker);
			Thread thread = new Thread(worker);
			pool.add(thread);
			thread.start();
		}
	}
	
	public void shutdown() {
		for (Worker worker : workers) {
			worker.shutdown();
		}
	}
	
	public void interrupt() {
		for (Thread thread : pool) {
			thread.interrupt();
		}
	}
}
