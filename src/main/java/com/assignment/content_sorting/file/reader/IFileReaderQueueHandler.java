package com.assignment.content_sorting.file.reader;

public interface IFileReaderQueueHandler {

	public void addLinetoQueue(String line) throws InterruptedException;
}
