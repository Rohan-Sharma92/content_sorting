package com.assignment.content_sorting.file.cache;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Class TempFileCache.
 * @author Rohan
 */
public class TempFileCache implements ITempFileCache {

	/** The temp files. */
	private final Map<String, File> tempFiles = new ConcurrentHashMap<>();

	/** The same character files. */
	private final Map<String, Set<File>> sameCharacterFiles = new ConcurrentHashMap<>();
	
	/** The completed temp files. */
	private final Set<String> completedTempFiles = ConcurrentHashMap.newKeySet();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getTempFile(String fileName) {
		return tempFiles.get(fileName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addTempFile(String filename, File file) {
		this.tempFiles.put(filename, file);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFileComplete(String fileName) {
		return completedTempFiles.contains(fileName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCompletedFile(String fileName) {
		this.completedTempFiles.add(fileName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addToPrefixCache(File file) {
		String key = String.valueOf(file.getName().charAt(0));
		this.sameCharacterFiles.computeIfAbsent(key, k -> new HashSet<File>()).add(file);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getTempFileNames() {
		LinkedList<String> orderedFileNames = new LinkedList<String>(tempFiles.keySet());
		Collections.sort(orderedFileNames, ((a, b) -> a.compareTo(b)));
		return orderedFileNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<File> getFragmentedTempFiles(String name) {
		return this.sameCharacterFiles.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void purgeTempFiles() {
		this.tempFiles.values().stream().forEach(file -> file.delete());
		this.tempFiles.clear();
		this.completedTempFiles.clear();
	}
}
