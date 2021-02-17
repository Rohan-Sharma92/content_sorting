package com.assignment.content_sorting.file.sort;

import java.io.File;
import java.util.Queue;

import com.assignment.content_sorting.file.reader.IFileWrapper;

/**
 * The Class SortedFileWrapper.
 */
public class SortedFileWrapper implements IFileWrapper {

	/** The file. */
	private final File file;
	
	/** The buffer. */
	private final Queue<String> buffer;

	/**
	 * Instantiates a new sorted file wrapper.
	 *
	 * @param file the file
	 * @param buffer the buffer
	 */
	public SortedFileWrapper(final File file,final Queue<String> buffer) {
		this.file = file;
		this.buffer = buffer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addLinetoQueue(String line) throws InterruptedException {
		this.buffer.add(line);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getFile() {
		return this.file;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getFileSize() {
		return this.file.length();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFileRead() {
		return this.buffer.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void markFileComplete() {
		//Not required
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBufferEmpty() {
		return this.buffer.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String readLine() throws InterruptedException {
		return this.buffer.poll();
	}

}
