package com.assignment.content_sorting.file.reader;

/**
 * The Interface IFileReaderQueueHandler.
 * @author Rohan
 */
public interface IFileReaderQueueHandler {

	/**
	 * Adds the lineto queue.
	 *
	 * @param line the line
	 * @throws InterruptedException the interrupted exception
	 */
	public void addLinetoQueue(String line) throws InterruptedException;
	
	/**
	 * Read line.
	 *
	 * @return the string
	 * @throws InterruptedException the interrupted exception
	 */
	public String readLine() throws InterruptedException;
}
