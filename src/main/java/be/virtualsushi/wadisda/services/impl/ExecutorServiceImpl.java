package be.virtualsushi.wadisda.services.impl;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import be.virtualsushi.wadisda.services.ExecutorService;

public class ExecutorServiceImpl implements ExecutorService {

	private Executor executor;

	public ExecutorServiceImpl() {
		this.executor = Executors.newFixedThreadPool(10);
	}

	@Override
	public void execute(Runnable runnable) {
		executor.execute(runnable);
	}

}
