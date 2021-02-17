package com.assignment.content_sorting.mocks;

import java.util.concurrent.CompletableFuture;

import com.assignment.content_sorting.file.reader.IFileWrapper;
import com.assignment.content_sorting.file.splitter.IFileProcessEnqueuer;

public class MockFileProcessEnqueuer implements IFileProcessEnqueuer {

	private IFileWrapper fileWrapper;

	@Override
	public CompletableFuture<Void> enqueue(IFileWrapper fileWrapper) {
		this.fileWrapper = fileWrapper;
		CompletableFuture<Void> completedFuture = CompletableFuture.completedFuture(null);
		return completedFuture;
	}

	public IFileWrapper getFile() {
		return fileWrapper;
	}

}
