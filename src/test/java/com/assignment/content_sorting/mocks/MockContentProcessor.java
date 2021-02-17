package com.assignment.content_sorting.mocks;

import java.util.concurrent.CompletableFuture;

import com.assignment.content_sorting.service.IContentProcessor;

public class MockContentProcessor implements IContentProcessor {

	private boolean isProcessed;
	private String processorName;
	private CompletableFuture<Void> completableFuture;
	private boolean isProgress;
	
	public MockContentProcessor(final String processorName) {
		this.processorName=processorName;
	}

	@Override
	public CompletableFuture<Void> process() {
		this.isProgress=true;
		completableFuture = new CompletableFuture<>();
		return completableFuture;
	}
	
	public boolean isProcessed() {
		return this.isProcessed;
	}

	public String getProcessorName() {
		return processorName;
	}
	
	public void markComplete() {
		this.isProcessed=true;
		this.completableFuture.complete(null);
	}

	public boolean isProgress() {
		return isProgress;
	}

}
