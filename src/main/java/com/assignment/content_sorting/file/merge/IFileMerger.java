package com.assignment.content_sorting.file.merge;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

/**
 * The Interface IFileMerger.
 * @author Rohan
 */
public interface IFileMerger {

	/**
	 * Merge.
	 *
	 * @param orderedFileNames the ordered file names
	 * @return the completable future
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	CompletableFuture<Void> merge(LinkedList<String> orderedFileNames) throws IOException;

}
