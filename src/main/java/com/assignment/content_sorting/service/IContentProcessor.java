package com.assignment.content_sorting.service;

import java.util.concurrent.CompletableFuture;

/**
 * The Interface IContentProcessor.
 * @author Rohan
 */
public interface IContentProcessor {
	
	/**
	 * Process.
	 *
	 * @return the completable future
	 */
	public CompletableFuture<Void> process();
}
