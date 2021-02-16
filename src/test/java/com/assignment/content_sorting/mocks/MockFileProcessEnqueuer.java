package com.assignment.content_sorting.mocks;

import java.util.concurrent.CompletableFuture;

import com.assignment.content_sorting.file.reader.IFileWrapper;
import com.assignment.content_sorting.file.splitter.IFileProcessEnqueuer;

public class MockFileProcessEnqueuer implements IFileProcessEnqueuer {

	private IFileWrapper fileWrapper;

	private IFileWrapper file;

	@Override
	public CompletableFuture<IFileWrapper> enqueue(IFileWrapper fileWrapper) {
		this.fileWrapper = fileWrapper;
		CompletableFuture<IFileWrapper> completedFuture = CompletableFuture.completedFuture(file);
		return completedFuture;
	}

	public IFileWrapper getFile() {
		return fileWrapper;
	}

	public void setExpectedFile(IFileWrapper file) {
		this.file = file;
	}

}
