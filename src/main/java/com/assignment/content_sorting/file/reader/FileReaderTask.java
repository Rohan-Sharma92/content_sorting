package com.assignment.content_sorting.file.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.assignment.content_sorting.file.common.IFileTask;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class FileReaderTask implements IFileTask<Boolean> {

	private final IFileWrapper fileWrapper;

	@Inject
	public FileReaderTask(@Assisted final IFileWrapper fileWrapper) {
		this.fileWrapper = fileWrapper;
	}

	@Override
	public Boolean call() throws Exception {
		return readFile();
	}

	private Boolean readFile() throws Exception {
		BufferedReader bufferedReader = null;
		try {
			File file = fileWrapper.getFile();
			bufferedReader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				this.fileWrapper.addLinetoQueue(line);
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
		return Boolean.TRUE;
	}

}
