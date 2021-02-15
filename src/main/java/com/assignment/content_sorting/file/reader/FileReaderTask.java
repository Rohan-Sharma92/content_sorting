package com.assignment.content_sorting.file.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;

public class FileReaderTask implements Callable<Boolean> {

	private final IFileReaderQueueHandler fileReaderQueueHandler;
	private final IFileWrapper fileWrapper;

	public FileReaderTask(final IFileWrapper fileWrapper, final IFileReaderQueueHandler fileProcessEnqueuer) {
		this.fileWrapper = fileWrapper;
		this.fileReaderQueueHandler = fileProcessEnqueuer;
	}

	@Override
	public Boolean call() throws Exception {
		return readFile();
	}

	private Boolean readFile() {
		BufferedReader bufferedReader = null;
		try {
			File file = fileWrapper.getFile();
			//bufferedReader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()));
			bufferedReader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				this.fileReaderQueueHandler.addLinetoQueue(line);
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bufferedReader != null)
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return Boolean.TRUE;
	}

}
