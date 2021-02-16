package com.assignment.content_sorting.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

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

		CompletableFuture<Void> splitResult = fileSplitter.process();
		splitResult.whenComplete((res, ex) -> {
			TimeMetric sortTimeMetric = new TimeMetric("Sort files");
			try {
				sortFiles(sortTimeMetric);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	private void sortFiles(TimeMetric timeMetric) throws IOException {
		CompletableFuture<Void> sortResult = fileSorter.process();
		sortResult.whenComplete((res, ex) -> {
			timeMetric.print();
			TimeMetric mergeTimeMetric = new TimeMetric("K-way Merge");
			try {
				mergeFiles(mergeTimeMetric);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}
	

	private void mergeFiles(TimeMetric timeMetric) throws IOException {
		CompletableFuture<Void> mergeResult = fileMerger.process();
		mergeResult.whenComplete((res, ex) -> {
			System.out.println("Files merged");
			timeMetric.print();
			try {
				TimeMetric processedTimeMetric = new TimeMetric("Final Merge");
				processFinalMerge(processedTimeMetric);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	private void processFinalMerge(
			TimeMetric processedTimeMetric) throws IOException {
		processedFileMerger.process().whenComplete((res, ex) -> {
			System.out.println("Files merged");
			tempFileCache.purgeTempFiles();
			processedTimeMetric.print();
		});
	}

}
