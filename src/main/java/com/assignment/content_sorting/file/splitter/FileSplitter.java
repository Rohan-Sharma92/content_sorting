package com.assignment.content_sorting.file.splitter;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.assignment.content_sorting.file.factories.IFileSplittingEnqueuerFactory;
import com.assignment.content_sorting.file.reader.IFileWrapper;
import com.assignment.content_sorting.file.reader.InputFileWrapper;
import com.assignment.content_sorting.properties.IServerConfig;
import com.assignment.content_sorting.service.IContentProcessor;
import com.google.inject.Inject;

public class FileSplitter implements IContentProcessor {

	private final IServerConfig config;
	private final IFileSplittingEnqueuerFactory fileSplittingEnqueuerFactory;

	@Inject
	public FileSplitter(final IServerConfig config, final IFileSplittingEnqueuerFactory fileSplittingEnqueuerFactory) {
		this.config = config;
		this.fileSplittingEnqueuerFactory = fileSplittingEnqueuerFactory;
	}

	@Override
	public CompletableFuture<Void> process() {
		//TimeMetric metric = new TimeMetric("splitter");
		File inputDir = Paths.get(config.getListenDirectory()).toFile();
		List<IFileWrapper> wrappedFiles = Arrays.asList(inputDir.listFiles()).stream()
				.map(file -> new InputFileWrapper(file)).sorted(new Comparator<IFileWrapper>() {
					public int compare(IFileWrapper a, IFileWrapper b) {
						return b.getFileSize() > a.getFileSize() ? 1 : 0;
					}
				}).collect(Collectors.toList());
		List<CompletableFuture<Void>> futures = wrappedFiles.stream().map(file -> process(file))
				.collect(Collectors.toList());
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[futures.size()]));
	}

	private CompletableFuture<Void> process(IFileWrapper file) {
		IFileProcessEnqueuer fileProcessEnqueuer = fileSplittingEnqueuerFactory.createFileSplittingExecutionRequest();
		return fileProcessEnqueuer.enqueue(file);
	}

}
