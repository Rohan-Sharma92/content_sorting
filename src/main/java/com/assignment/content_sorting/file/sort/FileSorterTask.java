package com.assignment.content_sorting.file.sort;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import com.assignment.content_sorting.file.reader.FileReaderTask;
import com.assignment.content_sorting.file.reader.IFileReaderQueueHandler;
import com.assignment.content_sorting.file.reader.InputFileWrapper;
import com.assignment.content_sorting.file.writer.FileWriterTask;

public class FileSorterTask implements Callable<Void>,IFileReaderQueueHandler {

	private final File inputFile;
	private final LinkedList<String> queue;
	public FileSorterTask(final File inputFile) {
		this.inputFile = inputFile;
		this.queue = new LinkedList<>();
	}
	@Override
	public Void call() throws Exception {
		new FileReaderTask(new InputFileWrapper(inputFile),this).call();
		Collections.sort(queue,(a,b) -> a.compareTo(b));
		new FileWriterTask().writeLines(queue, inputFile, false);
		return null;
	}
	@Override
	public void addLinetoQueue(String line) throws InterruptedException {
		queue.add(line);
	}

}
