package com.assignment.content_sorting.file.sort;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

import javax.inject.Named;

import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.service.IContentProcessor;
import com.google.inject.Inject;

public class FileSorter implements IContentProcessor {

	private final ITempFileCache tempFileCache;
	private final ExecutorService threadPool;

	@Inject
	public FileSorter(final ITempFileCache tempFileCache,
			final @Named("FileSplittingExecutor") ExecutorService threadPool) {
		this.tempFileCache = tempFileCache;
		this.threadPool = threadPool;
	}

	@Override
	public CompletableFuture<Void> process() {
		LinkedList<String> orderedFileNames = new LinkedList<String>(tempFileCache.getTempFileNames());
		List<CompletableFuture<Void>> futures = new ArrayList<>();
		Collections.sort(orderedFileNames, ((a, b) -> a.compareTo(b)));
		orderedFileNames.stream().forEach(name -> {
			File tempFile = tempFileCache.getTempFile(name);
			futures.add(CompletableFuture.<Void>supplyAsync(() -> {
				try {
					return new FileSorterTask(tempFile).call();
				} catch (Exception e) {
					throw new CompletionException(e);
				}
			}, threadPool));
		});
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
	}

}
