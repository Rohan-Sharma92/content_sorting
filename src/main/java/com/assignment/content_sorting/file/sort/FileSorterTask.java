package com.assignment.content_sorting.file.sort;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;

import com.assignment.content_sorting.file.common.IFileTask;
import com.assignment.content_sorting.file.factories.IFileReaderTaskFactory;
import com.assignment.content_sorting.file.writer.FileWriterTask;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * The Class FileSorterTask.
 * @author Rohan
 */
public class FileSorterTask implements IFileTask<Void> {

	/** The input file. */
	private final File inputFile;
	
	/** The queue. */
	private final LinkedList<String> queue;
	
	/** The file writer task. */
	private final FileWriterTask fileWriterTask;
	
	/** The reader task factory. */
	private final IFileReaderTaskFactory readerTaskFactory;
	
	/**
	 * Instantiates a new file sorter task.
	 *
	 * @param inputFile the input file
	 * @param readerTaskFactory the reader task factory
	 */
	@Inject
	public FileSorterTask(@Assisted final File inputFile, final IFileReaderTaskFactory readerTaskFactory) {
		this.inputFile = inputFile;
		this.readerTaskFactory = readerTaskFactory;
		this.queue = new LinkedList<>();
		this.fileWriterTask= new FileWriterTask();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void call() throws Exception {
		SortedFileWrapper fileWrapper = new SortedFileWrapper(inputFile,queue);
		readerTaskFactory.createReader(fileWrapper).call();
		Collections.sort(queue,(a,b) -> a.compareTo(b));
		fileWriterTask.writeLines(queue, inputFile, false);
		return null;
	}

}
