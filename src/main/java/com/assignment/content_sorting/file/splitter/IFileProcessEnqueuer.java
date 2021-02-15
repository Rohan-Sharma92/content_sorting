package com.assignment.content_sorting.file.splitter;

import java.util.concurrent.CompletableFuture;

import com.assignment.content_sorting.file.reader.IFileWrapper;

public interface IFileProcessEnqueuer{

	public CompletableFuture<IFileWrapper> enqueue(final IFileWrapper fileWrapper);
	
	public String readLine() throws InterruptedException;
	
	public boolean isBufferEmpty();
	
	public boolean isFileRead();
	
}
