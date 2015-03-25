package ua.koxa.server;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface ExecutorService extends Executor {

	void shutdown();
	List<Task> shutdownNow();
	boolean isShutdown();
	boolean isTerminated();
	boolean awaitTermination(long timeout, TimeUnit unit);
}
