package com.assignment.content_sorting.file.sort;

import java.io.File;
import java.util.Queue;

import com.assignment.content_sorting.file.reader.IFileWrapper;

public class SortedFileWrapper implements IFileWrapper {

	private final File file;
	
	private final Queue<String> buffer;

	public SortedFileWrapper(final File file,final Queue<String> buffer) {
		this.file = file;
		this.buffer = buffer;
	}
	@Override
	public void addLinetoQueue(String line) throws InterruptedException {
		this.buffer.add(line);
	}

	@Override
	public File getFile() {
		return this.file;
	}

	@Override
	public long getFileSize() {
		return this.file.length();
	}

	@Override
	public boolean isFileRead() {
		return this.buffer.isEmpty();
	}

	@Override
	public void markFileComplete() {
		//Not required
	}

	@Override
	public boolean isBufferEmpty() {
		return this.buffer.isEmpty();
	}

	@Override
	public String readLine() throws InterruptedException {
		return this.buffer.poll();
	}

}
