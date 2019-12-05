package sample.netty;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
	final AtomicInteger threadNumber = new AtomicInteger(1);
	final String name;
	final boolean deamon;

	public NamedThreadFactory(final String name, final boolean deamon) {
		this.name = name;
		this.deamon = deamon;
	}

	public Thread newThread(final Runnable runnable) {
		final Thread t = new Thread(runnable, name + "-thread-" + threadNumber.getAndIncrement());
		t.setDaemon(deamon);

		return t;
	}
}
