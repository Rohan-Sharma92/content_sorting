package com.assignment.content_sorting.file.sort;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

import javax.inject.Named;

import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.file.factories.IFileSorterTaskFactory;
import com.assignment.content_sorting.service.IContentProcessor;
import com.google.inject.Inject;

public class FileSorter implements IContentProcessor {

	private final ITempFileCache tempFileCache;
	private final ExecutorService threadPool;
	private final IFileSorterTaskFactory fileSorterTaskFactory;

	@Inject
	public FileSorter(final ITempFileCache tempFileCache,
			final @Named("FileSplittingExecutor") ExecutorService threadPool,
			final IFileSorterTaskFactory fileSorterTaskFactory) {
		this.tempFileCache = tempFileCache;
		this.threadPool = threadPool;
		this.fileSorterTaskFactory = fileSorterTaskFactory;
	}

	@Override
	public CompletableFuture<Void> process() {
		List<CompletableFuture<Void>> futures = new ArrayList<>();
		tempFileCache.getTempFileNames().stream().forEach(name -> {
			File tempFile = tempFileCache.getTempFile(name);
			futures.add(CompletableFuture.<Void>supplyAsync(() -> {
				try {
					return fileSorterTaskFactory.createSorter(tempFile)
							.call();
				} catch (Exception e) {
					throw new CompletionException(e);
				}
			}, threadPool));
		});
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
	}

}
