package ua.koxa.server;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class FixedThreadPoolExecutorService implements ExecutorService {

	private final BlockingQueue<Task> workQueue;
	private volatile boolean isActive = true;
	private volatile boolean isTerminated = false;
	
	private final ThreadPool threadPool;
			
	public FixedThreadPoolExecutorService(int threadNumber, int maxTasksNumber) {
		workQueue = new ArrayBlockingQueue<Task>(maxTasksNumber);
		threadPool = new ThreadPool(threadNumber, workQueue);
	}
	
	@Override
	public void execute(Task task) {
		if (isActive) {
			workQueue.add(task);
		}
	}

	@Override
	public void shutdown() {
		threadPool.shutdown();
		isActive = false;
	}

	@Override
	public List<Task> shutdownNow() {
		threadPool.interrupt();
		return null;
	}

	@Override
	public boolean isShutdown() {
		return !isActive;
	}

	@Override
	public boolean isTerminated() {
		return isTerminated;
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) {
		isTerminated = true;
		return false;
	}

}
