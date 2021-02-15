package com.assignment.content_sorting.file.cache;

import java.io.File;
import java.util.Set;

public interface ITempFileCache {
	public File getTempFile(String fileName);

	public void addTempFile(String filename, File file);

	public boolean isFileComplete(String fileName);

	public void addCompletedFile(String fileName);

	public void addToPrefixCache(File file);

	public Set<String> getTempFileNames();

	public Set<File> getFragmentedTempFiles(String name);

	public void purgeTempFiles();
}
