package com.assignment.content_sorting.file.reader;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class InputFileWrapper implements IFileWrapper {

	private final File file;
	private final AtomicBoolean isFileRead= new AtomicBoolean();
	private final BlockingQueue<String> lineQueue = new ArrayBlockingQueue<String>(100);

	public InputFileWrapper(final File file) {
		this.file = file;
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public long getFileSize() {
		return file.getTotalSpace();
	}

	@Override
	public boolean isFileRead() {
		return this.isFileRead.get();
	}

	@Override
	public void markFileComplete() {
		this.isFileRead.getAndSet(true);
	}
	@Override
	public void addLinetoQueue(String line) throws InterruptedException {
		lineQueue.put(line);
	}

	@Override
	public String readLine() throws InterruptedException {
		return lineQueue.poll(50, TimeUnit.MILLISECONDS);
	}

	@Override
	public boolean isBufferEmpty() {
		return lineQueue.isEmpty();
	}
}
