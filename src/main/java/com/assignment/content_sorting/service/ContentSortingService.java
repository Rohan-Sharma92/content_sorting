package com.assignment.content_sorting.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import javax.inject.Named;

import com.assignment.content_sorting.util.TimeMetric;
import com.google.inject.Inject;

public class ContentSortingService extends AbstractDependentService {

	private final IContentProcessor fileSplitter;
	private final IContentProcessor fileSorter;

	@Inject
	public ContentSortingService(final @Named("FileSplitter") IContentProcessor fileSplitter,
			final @Named("FileSorter") IContentProcessor fileSorter) {
		this.fileSplitter = fileSplitter;
		this.fileSorter = fileSorter;
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

		// timeMetric = new TimeMetric("File processing");
		// logger.info(this.getClass().getSimpleName());
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
			System.out.println("Files sorted");
		});
//			timeMetric.print();
//			TimeMetric mergeTimeMetric = new TimeMetric("K-way Merge");
//			try {
//				mergeFiles(tempFileCache, mergeTimeMetric);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});

	}

}
