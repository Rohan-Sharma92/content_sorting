package com.assignment.content_sorting.file.reader;

import java.io.File;

/**
 * The Interface IFileWrapper.
 */
public interface IFileWrapper extends IFileReaderQueueHandler{

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile();
	
	/**
	 * Gets the file size.
	 *
	 * @return the file size
	 */
	public long getFileSize();
	
	/**
	 * Checks if is file read.
	 *
	 * @return true, if is file read
	 */
	public boolean isFileRead();
	
	/**
	 * Mark file complete.
	 */
	public void markFileComplete();

	/**
	 * Checks if is buffer empty.
	 *
	 * @return true, if is buffer empty
	 */
	public boolean isBufferEmpty();
}
