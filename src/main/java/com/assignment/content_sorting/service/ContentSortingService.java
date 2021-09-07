package com.assignment.content_sorting.service;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;

import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.util.TimerTask;
import com.google.inject.Inject;

/**
 * The Class ContentSortingService.
 * @author Rohan
 */
public class ContentSortingService extends AbstractDependentService {

	private static final String LOG_KEY = "Content Sorting Process";

	/** The file splitter. */
	private final IContentProcessor fileSplitter;
	
	/** The file sorter. */
	private final IContentProcessor fileSorter;
	
	/** The file merger. */
	private final IContentProcessor fileMerger;
	
	/** The processed file merger. */
	private final IContentProcessor processedFileMerger;
	
	/** The temp file cache. */
	private final ITempFileCache tempFileCache;
	
	/** The timer task. */
	private final TimerTask timerTask;

	private final Logger logger;

	/**
	 * Instantiates a new content sorting service.
	 *
	 * @param fileSplitter the file splitter
	 * @param fileSorter the file sorter
	 * @param fileMerger the file merger
	 * @param processedFileMerger the processed file merger
	 * @param tempFileCache the temp file cache
	 */
	@Inject
	public ContentSortingService(final @Named("FileSplitter") IContentProcessor fileSplitter,
			final @Named("FileSorter") IContentProcessor fileSorter,
			final @Named("TempFileMerge") IContentProcessor fileMerger,
			final @Named("ProcessedFileMerge") IContentProcessor processedFileMerger,
			final ITempFileCache tempFileCache,
			final @Named("AppLogger") Logger logger) {
		this.fileSplitter = fileSplitter;
		this.fileSorter = fileSorter;
		this.fileMerger = fileMerger;
		this.processedFileMerger = processedFileMerger;
		this.tempFileCache = tempFileCache;
		this.logger = logger;
		this.timerTask = new TimerTask(LOG_KEY,logger);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performProcessing() {
		/**
		 * Processing logic consists of 4 stages:
		 * 
		 * 1. Split files 2. Sort Files 3. Merge sorted temp files into k files 4. Merge
		 * k files into output file
		 * 
		 */
		logger.info("Started processing");
		timerTask.start();
		final CompletableFuture<Void> splitResult = fileSplitter.process();
		splitResult.whenComplete((res, ex) -> {
			timerTask.completeStage("Split files");
			if (ex != null) {
				handleException("Exception encountered while splitting: ", ex);
			} else {
				sortFiles();
			}
		});

	}

	/**
	 * Handle exception.
	 *
	 * @param msg the msg
	 * @param ex the ex
	 */
	private void handleException(String msg, Throwable ex) {
		logger.log(Level.SEVERE, msg, ex);
	}

	/**
	 * Sort files.
	 */
	private void sortFiles() {
		final CompletableFuture<Void> sortResult = fileSorter.process();
		sortResult.whenComplete((res, ex) -> {
			timerTask.completeStage("Sort files");
			if (ex != null) {
				handleException("Exception encountered while sorting: ", ex);
			} else {
				mergeFiles();
			}
		});

	}

	/**
	 * Merge files.
	 */
	private void mergeFiles() {
		final CompletableFuture<Void> mergeResult = fileMerger.process();
		mergeResult.whenComplete((res, ex) -> {
			timerTask.completeStage("K-way Merge");
			if (ex != null) {
				handleException("Exception encountered while merge: ", ex);
			} else {
				processFinalMerge();
			}
		});

	}

	/**
	 * Process final merge.
	 */
	private void processFinalMerge() {
		processedFileMerger.process().whenComplete((res, ex) -> {
			timerTask.completeStage("Final Merge");
			if (ex != null) {
				handleException("Exception encountered while final merge: ", ex);
			} else {
				tempFileCache.purgeTempFiles();
			}
			timerTask.processingComplete();
			System.out.println("Processing complete");
		});
	}

}
