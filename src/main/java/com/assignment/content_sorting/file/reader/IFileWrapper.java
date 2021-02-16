package com.assignment.content_sorting.file.reader;

import java.io.File;

public interface IFileWrapper extends IFileReaderQueueHandler{

	public File getFile();
	
	public long getFileSize();
	
	public boolean isFileRead();
	
	public void markFileComplete();

	public boolean isBufferEmpty();
}
