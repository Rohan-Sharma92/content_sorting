package com.assignment.content_sorting.util;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The Class BufferedReaderWrapper.
 */
public class BufferedReaderWrapper implements Comparable<BufferedReaderWrapper> {
	
	/** The buffered reader. */
	private BufferedReader bufferedReader;
	
	/** The line. */
	private String line;

	/**
	 * Instantiates a new buffered reader wrapper.
	 *
	 * @param bufferedReader the buffered reader
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public BufferedReaderWrapper(BufferedReader bufferedReader) throws IOException {
		this.bufferedReader = bufferedReader;
		readNextLine();
	}

	/**
	 * Read next line.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void readNextLine() throws IOException {
		this.line = bufferedReader.readLine();
	}

	/**
	 * Next.
	 *
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String next() throws IOException {
		String answer = line;
		readNextLine();
		return answer;
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return this.line == null;
	}

	/**
	 * Gets the comparable.
	 *
	 * @return the comparable
	 */
	public String getComparable() {
		return this.line;
	}

	/**
	 * Close.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void close() throws IOException {
		this.bufferedReader.close();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(BufferedReaderWrapper o) {
		if (this.getComparable() == null)
			return 1;
		if (o.getComparable() == null)
			return -1;
		return this.getComparable().compareTo(o.getComparable());
	}
}
