package com.assignment.content_sorting.file.splitter;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Named;

import com.assignment.content_sorting.file.factories.IFileSplittingEnqueuerFactory;
import com.assignment.content_sorting.file.reader.IFileWrapper;
import com.assignment.content_sorting.file.reader.InputFileWrapper;
import com.assignment.content_sorting.properties.IServerConfig;
import com.assignment.content_sorting.service.IContentProcessor;
import com.google.inject.Inject;

/**
 * The Class FileSplitter.
 */
public class FileSplitter implements IContentProcessor {

	/** The config. */
	private final IServerConfig config;
	
	/** The file splitting enqueuer factory. */
	private final IFileSplittingEnqueuerFactory fileSplittingEnqueuerFactory;

	private final Logger logger;

	/**
	 * Instantiates a new file splitter.
	 *
	 * @param config the config
	 * @param fileSplittingEnqueuerFactory the file splitting enqueuer factory
	 */
	@Inject
	public FileSplitter(final IServerConfig config, final IFileSplittingEnqueuerFactory fileSplittingEnqueuerFactory,
			final @Named("AppLogger") Logger logger) {
		this.config = config;
		this.fileSplittingEnqueuerFactory = fileSplittingEnqueuerFactory;
		this.logger = logger;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompletableFuture<Void> process() {
		File inputDir = Paths.get(config.getListenDirectory()).toFile();
		List<IFileWrapper> wrappedFiles = Arrays.asList(inputDir.listFiles()).stream()
				.map(file -> new InputFileWrapper(file)).sorted(new Comparator<IFileWrapper>() {
					public int compare(IFileWrapper a, IFileWrapper b) {
						return b.getFileSize() > a.getFileSize() ? 1 : 0;
					}
				}).collect(Collectors.toList());
		logger.log(Level.INFO,"Splitting files..."+ "Count:"+wrappedFiles.size());
		List<CompletableFuture<Void>> futures = wrappedFiles.stream().map(file -> process(file))
				.collect(Collectors.toList());
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[futures.size()]));
	}

	/**
	 * Process.
	 *
	 * @param file the file
	 * @return the completable future
	 */
	private CompletableFuture<Void> process(IFileWrapper file) {
		IFileProcessEnqueuer fileProcessEnqueuer = fileSplittingEnqueuerFactory.createFileSplittingExecutionRequest();
		return fileProcessEnqueuer.enqueue(file);
	}

}
