package com.assignment.content_sorting.file.reader;

import java.io.File;

public class InputFileWrapper implements IFileWrapper{

	private final File file;

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
}
