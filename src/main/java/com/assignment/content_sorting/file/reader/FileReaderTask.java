package com.assignment.content_sorting.file.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.assignment.content_sorting.exceptions.ContentSortingException;
import com.assignment.content_sorting.file.common.IFileTask;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class FileReaderTask implements IFileTask<Void> {

	private final IFileWrapper fileWrapper;

	@Inject
	public FileReaderTask(@Assisted final IFileWrapper fileWrapper) {
		this.fileWrapper = fileWrapper;
	}

	@Override
	public Void call() throws Exception {
		return readFile();
	}

	private Void readFile() throws Exception {
		BufferedReader bufferedReader = null;
		try {
			File file = fileWrapper.getFile();
			bufferedReader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if (isValid(line))
					this.fileWrapper.addLinetoQueue(line);
				else {
					this.fileWrapper.markFileComplete();
					throw new ContentSortingException("Invalid line. Only alphanumeric characters allowed");					
				}
			}
			this.fileWrapper.markFileComplete();
		} catch (IOException | InterruptedException e) {
			throw e;
		} finally {
			if (bufferedReader != null)
				try {
					bufferedReader.close();
				} catch (IOException e) {
					throw e;
				}
		}
		return null;
	}

	private boolean isValid(String line) {
		return line.matches("[A-Za-z0-9]+");
	}

}
