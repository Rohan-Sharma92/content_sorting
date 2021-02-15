package com.assignment.content_sorting.file.splitter;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import javax.inject.Named;

import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.file.reader.IFileWrapper;
import com.assignment.content_sorting.file.reader.InputFileWrapper;
import com.assignment.content_sorting.properties.IServerConfig;
import com.assignment.content_sorting.service.IContentProcessor;
import com.assignment.content_sorting.util.TimeMetric;
import com.google.inject.Inject;

public class FileSplitter implements IContentProcessor{

	private final ITempFileCache tempFileCache;
	private final ExecutorService executorService;
	private final IServerConfig config;

	@Inject
	public FileSplitter(final ITempFileCache tempFileCache,
			final @Named("FileSplittingExecutor") ExecutorService executorService, final IServerConfig config) {
		this.tempFileCache = tempFileCache;
		this.executorService = executorService;
		this.config = config;
	}

	@Override
	public CompletableFuture<Void> process() {
		TimeMetric metric = new TimeMetric("splitter");
		File inputDir = Paths.get(config.getListenDirectory()).toFile();
		List<IFileWrapper> wrappedFiles = Arrays.asList(inputDir.listFiles()).stream()
				.map(file -> new InputFileWrapper(file)).sorted(new Comparator<IFileWrapper>() {
					public int compare(IFileWrapper a, IFileWrapper b) {
						return b.getFileSize() > a.getFileSize() ? 1 : 0;
					}
				}).collect(Collectors.toList());
		List<CompletableFuture<IFileWrapper>> futures = wrappedFiles.stream()
				.map(file -> process(file, executorService)).collect(Collectors.toList());
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[futures.size()]))
				.whenComplete((result, ex) -> {
					System.out.print("File splitted");
					metric.print();
				});
	}

	private CompletableFuture<IFileWrapper> process(IFileWrapper file, ExecutorService threadPool) {
		IFileProcessEnqueuer fileProcessEnqueuer = new FileSplittingEnqueuer(threadPool, tempFileCache, config);
		return fileProcessEnqueuer.enqueue(file);
	}

}
