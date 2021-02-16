package com.assignment.content_sorting.mocks;

import java.util.concurrent.CompletableFuture;

import com.assignment.content_sorting.file.reader.IFileWrapper;
import com.assignment.content_sorting.file.splitter.IFileProcessEnqueuer;

public class MockFileProcessEnqueuer implements IFileProcessEnqueuer {

	private IFileWrapper fileWrapper;

	private boolean isInvoked;

	private IFileWrapper file;

	@Override
	public CompletableFuture<IFileWrapper> enqueue(IFileWrapper fileWrapper) {
		this.fileWrapper = fileWrapper;
		this.isInvoked = true;
		CompletableFuture<IFileWrapper> completedFuture = CompletableFuture.completedFuture(file);
		return completedFuture;
	}

	public IFileWrapper getFile() {
		return fileWrapper;
	}

	@Override
	public String readLine() throws InterruptedException {
		return null;
	}

	@Override
	public boolean isBufferEmpty() {
		return false;
	}

	@Override
	public boolean isFileRead() {
		return false;
	}

	public boolean isInvoked() {
		return isInvoked;
	}

	public void setExpectedFile(IFileWrapper file) {
		this.file = file;
	}

}
