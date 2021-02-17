package com.assignment.content_sorting.file.factories;

import com.assignment.content_sorting.file.splitter.IFileProcessEnqueuer;

/**
 * A factory for creating IFileSplittingEnqueuer objects.
 */
public interface IFileSplittingEnqueuerFactory {

	/**
	 * Creates a new IFileSplittingEnqueuer object.
	 *
	 * @return the i file process enqueuer
	 */
	IFileProcessEnqueuer createFileSplittingExecutionRequest();
}
