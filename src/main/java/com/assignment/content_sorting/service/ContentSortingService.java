package com.assignment.content_sorting.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import javax.inject.Named;

import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.util.TimeMetric;
import com.google.inject.Inject;

public class ContentSortingService extends AbstractDependentService {

	private final IContentProcessor fileSplitter;
	private final IContentProcessor fileSorter;
	private final IContentProcessor fileMerger;
	private final IContentProcessor processedFileMerger;
	private final ITempFileCache tempFileCache;

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

		final CompletableFuture<Void> splitResult = fileSplitter.process();
		splitResult.whenComplete((res, ex) -> {
			if (ex != null)
				throw new CompletionException("Exception encountered while splitting", ex);
			final TimeMetric sortTimeMetric = new TimeMetric("Sort files");
			sortFiles(sortTimeMetric);
		});

	}

	private void sortFiles(final TimeMetric timeMetric) {
		final CompletableFuture<Void> sortResult = fileSorter.process();
		sortResult.whenComplete((res, ex) -> {
			if (ex != null)
				throw new CompletionException("Exception encountered while sorting", ex);
			timeMetric.print();
			final TimeMetric mergeTimeMetric = new TimeMetric("K-way Merge");
			mergeFiles(mergeTimeMetric);
		});

	}

	private void mergeFiles(final TimeMetric timeMetric) {
		final CompletableFuture<Void> mergeResult = fileMerger.process();
		mergeResult.whenComplete((res, ex) -> {
			if (ex != null) {
				throw new CompletionException("Exception encountered while merge", ex);
			} else {
				System.out.println("Files merged");
				timeMetric.print();
				final TimeMetric processedTimeMetric = new TimeMetric("Final Merge");
				processFinalMerge(processedTimeMetric);
			}
		});

	}

	private void processFinalMerge(final TimeMetric processedTimeMetric) {
		processedFileMerger.process().whenComplete((res, ex) -> {
			System.out.println("Files merged");
			tempFileCache.purgeTempFiles();
			processedTimeMetric.print();
		});
	}

}
