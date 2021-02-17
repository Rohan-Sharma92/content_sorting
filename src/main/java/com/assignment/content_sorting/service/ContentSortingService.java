package com.assignment.content_sorting.service;

import java.util.concurrent.CompletableFuture;

import javax.inject.Named;

import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.util.TimerTask;
import com.google.inject.Inject;

public class ContentSortingService extends AbstractDependentService {

	private final IContentProcessor fileSplitter;
	private final IContentProcessor fileSorter;
	private final IContentProcessor fileMerger;
	private final IContentProcessor processedFileMerger;
	private final ITempFileCache tempFileCache;
	private final TimerTask timerTask;

	@Inject
	public ContentSortingService(final @Named("FileSplitter") IContentProcessor fileSplitter,
			final @Named("FileSorter") IContentProcessor fileSorter,
			final @Named("TempFileMerge") IContentProcessor fileMerger,
			final @Named("ProcessedFileMerge") IContentProcessor processedFileMerger,
			final ITempFileCache tempFileCache) {
		this.fileSplitter = fileSplitter;
		this.fileSorter = fileSorter;
		this.fileMerger = fileMerger;
		this.processedFileMerger = processedFileMerger;
		this.tempFileCache = tempFileCache;
		this.timerTask = new TimerTask("Content Sorting Process");
	}

	@Override
	protected void performProcessing() {
		/**
		 * Processing logic consists of 4 stages:
		 * 
		 * 1. Split files 2. Sort Files 3. Merge sorted temp files into k files 4. Merge
		 * k files into output file
		 * 
		 */
		System.out.println("Started processing");
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

	private void handleException(String msg, Throwable ex) {
		System.out.println(msg + ex.getMessage());
	}

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
