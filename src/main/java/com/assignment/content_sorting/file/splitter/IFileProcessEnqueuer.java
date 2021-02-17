package com.assignment.content_sorting.file.splitter;

import java.util.concurrent.CompletableFuture;

import com.assignment.content_sorting.file.reader.IFileWrapper;

/**
 * The Interface IFileProcessEnqueuer.
 * @author Rohan
 */
public interface IFileProcessEnqueuer {

	/**
	 * Enqueue.
	 *
	 * @param fileWrapper the file wrapper
	 * @return the completable future
	 */
	public CompletableFuture<Void> enqueue(final IFileWrapper fileWrapper);

}
