package com.assignment.content_sorting.file.cache;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TempFileCache implements ITempFileCache {
	private final Map<String, File> tempFiles = new ConcurrentHashMap<>();
	private final Map<String, Set<File>> sameCharacterFiles = new ConcurrentHashMap<>();
	private final Set<String> exhaustedTempFiles = ConcurrentHashMap.newKeySet();

	@Override
	public File getTempFile(String fileName) {
		return tempFiles.get(fileName);
	}

	@Override
	public void addTempFile(String filename, File file) {
		this.tempFiles.put(filename, file);
	}

	@Override
	public boolean isFileComplete(String fileName) {
		return exhaustedTempFiles.contains(fileName);
	}

	@Override
	public void addCompletedFile(String fileName) {
		this.exhaustedTempFiles.add(fileName);
	}

	@Override
	public void addToPrefixCache(File file) {
		String key = String.valueOf(file.getName().charAt(0));
		this.sameCharacterFiles.computeIfAbsent(key, k -> new HashSet<File>()).add(file);
	}

	@Override
	public List<String> getTempFileNames() {
		LinkedList<String> orderedFileNames = new LinkedList<String>(tempFiles.keySet());
		Collections.sort(orderedFileNames, ((a, b) -> a.compareTo(b)));
		return orderedFileNames;
	}

	@Override
	public Set<File> getFragmentedTempFiles(String name) {
		return this.sameCharacterFiles.get(name);
	}

	@Override
	public void purgeTempFiles() {
		this.tempFiles.values().stream().forEach(file ->file.delete());
		this.tempFiles.clear();
		this.exhaustedTempFiles.clear();
	}
}
