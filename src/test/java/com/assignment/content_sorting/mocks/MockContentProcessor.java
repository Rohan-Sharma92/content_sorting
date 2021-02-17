package com.assignment.content_sorting.mocks;

import java.util.concurrent.CompletableFuture;

import com.assignment.content_sorting.service.IContentProcessor;

public class MockContentProcessor implements IContentProcessor {

	private boolean isProcessed;
	private CompletableFuture<Void> completableFuture;
	private boolean isProgress;
	private Exception e;

	@Override
	public CompletableFuture<Void> process() {
		if (e!=null) {
			completableFuture = new CompletableFuture<>();
			completableFuture.completeExceptionally(e);
			return completableFuture;
		}
		this.isProgress = true;
		completableFuture = new CompletableFuture<>();
		return completableFuture;
	}

	public boolean isProcessed() {
		return this.isProcessed;
	}

	public void markComplete() {
		this.isProcessed = true;
		this.completableFuture.complete(null);
	}

	public boolean isProgress() {
		return isProgress;
	}

	public void setExceptionProcessor(Exception e) {
		this.e = e;
	}

}
