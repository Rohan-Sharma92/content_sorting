package com.assignment.content_sorting.file.cache;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * The Interface ITempFileCache.
 * @author Rohan
 */
public interface ITempFileCache {
	
	/**
	 * Gets the temp file.
	 *
	 * @param fileName the file name
	 * @return the temp file
	 */
	public File getTempFile(String fileName);

	/**
	 * Adds the temp file.
	 *
	 * @param filename the filename
	 * @param file the file
	 */
	public void addTempFile(String filename, File file);

	/**
	 * Checks if is file complete.
	 *
	 * @param fileName the file name
	 * @return true, if is file complete
	 */
	public boolean isFileComplete(String fileName);

	/**
	 * Adds the completed file.
	 *
	 * @param fileName the file name
	 */
	public void addCompletedFile(String fileName);

	/**
	 * Adds the to prefix cache.
	 *
	 * @param file the file
	 */
	public void addToPrefixCache(File file);

	/**
	 * Gets the temp file names.
	 *
	 * @return the temp file names
	 */
	public List<String> getTempFileNames();

	/**
	 * Gets the fragmented temp files.
	 *
	 * @param name the name
	 * @return the fragmented temp files
	 */
	public Set<File> getFragmentedTempFiles(String name);

	/**
	 * Purge temp files.
	 */
	public void purgeTempFiles();
}
