package com.assignment.content_sorting.file.reader;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The Class InputFileWrapper.
 * 
 * @author Rohan
 */
public class InputFileWrapper implements IFileWrapper {

	/** The file. */
	private final File file;

	/** The is file read. */
	private final AtomicBoolean isFileRead = new AtomicBoolean();

	/** The line queue. */
	private final BlockingQueue<String> lineQueue = new ArrayBlockingQueue<String>(100);

	/**
	 * Instantiates a new input file wrapper.
	 *
	 * @param file the file
	 */
	public InputFileWrapper(final File file) {
		this.file = file;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getFile() {
		return file;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getFileSize() {
		return file.getTotalSpace();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFileRead() {
		return this.isFileRead.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void markFileComplete() {
		this.isFileRead.getAndSet(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addLinetoQueue(String line) throws InterruptedException {
		lineQueue.put(line);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String readLine() throws InterruptedException {
		return lineQueue.poll(50, TimeUnit.MILLISECONDS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBufferEmpty() {
		return lineQueue.isEmpty();
	}
}
