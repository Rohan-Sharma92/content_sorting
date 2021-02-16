package com.assignment.content_sorting.file.sort;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;

import com.assignment.content_sorting.file.common.IFileTask;
import com.assignment.content_sorting.file.factories.IFileReaderTaskFactory;
import com.assignment.content_sorting.file.writer.FileWriterTask;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class FileSorterTask implements IFileTask<Void> {

	private final File inputFile;
	private final LinkedList<String> queue;
	private final FileWriterTask fileWriterTask;
	private final IFileReaderTaskFactory readerTaskFactory;
	@Inject
	public FileSorterTask(@Assisted final File inputFile, final IFileReaderTaskFactory readerTaskFactory) {
		this.inputFile = inputFile;
		this.readerTaskFactory = readerTaskFactory;
		this.queue = new LinkedList<>();
		this.fileWriterTask= new FileWriterTask();
	}
	@Override
	public Void call() throws Exception {
		SortedFileWrapper fileWrapper = new SortedFileWrapper(inputFile,queue);
		readerTaskFactory.createReader(fileWrapper).call();
		Collections.sort(queue,(a,b) -> a.compareTo(b));
		fileWriterTask.writeLines(queue, inputFile, false);
		return null;
	}

}
