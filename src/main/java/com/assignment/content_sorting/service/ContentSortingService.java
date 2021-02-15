package com.assignment.content_sorting.service;

import java.util.concurrent.CompletableFuture;

import javax.inject.Named;

import com.google.inject.Inject;

public class ContentSortingService extends AbstractDependentService {

	private final IContentProcessor fileSplitter;

	@Inject
	public ContentSortingService(final @Named("FileSplitter") IContentProcessor fileSplitter) {
		this.fileSplitter = fileSplitter;
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
			System.out.print("Completed");
		});
//			TimeMetric sortTimeMetric = new TimeMetric("Sort files");
//			try {
//				sortFiles(tempFileCache, sortTimeMetric);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});

	}

}
