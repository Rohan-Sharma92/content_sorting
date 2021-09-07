package com.assignment.content_sorting.file.splitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;

import com.assignment.content_sorting.file.common.IFileTask;
import com.assignment.content_sorting.file.factories.IFileReaderTaskFactory;
import com.assignment.content_sorting.file.factories.IFileSplitterTaskFactory;
import com.assignment.content_sorting.file.reader.IFileWrapper;
import com.assignment.content_sorting.properties.IServerConfig;
import com.google.inject.Inject;

/**
 * The Class FileSplittingEnqueuer.
 * @author Rohan
 */
public class FileSplittingEnqueuer implements IFileProcessEnqueuer {

	/** The file splitting pool. */
	private final ExecutorService fileSplittingPool;
	
	/** The config. */
	private final IServerConfig config;
	
	/** The file splitter task factory. */
	private final IFileSplitterTaskFactory fileSplitterTaskFactory;
	
	/** The file reader task factory. */
	private final IFileReaderTaskFactory fileReaderTaskFactory;

	private final Logger logger;

	/**
	 * Instantiates a new file splitting enqueuer.
	 *
	 * @param fileSplittingPool the file splitting pool
	 * @param config the config
	 * @param fileReaderTaskFactory the file reader task factory
	 * @param fileSplitterTaskFactory the file splitter task factory
	 */
	@Inject
	public FileSplittingEnqueuer(@Named("ContentSortingExecutor") final ExecutorService fileSplittingPool,
			final IServerConfig config, final IFileReaderTaskFactory fileReaderTaskFactory,
			final IFileSplitterTaskFactory fileSplitterTaskFactory,
			final @Named("AppLogger") Logger logger) {
		this.fileSplittingPool = fileSplittingPool;
		this.config = config;
		this.fileReaderTaskFactory = fileReaderTaskFactory;
		this.fileSplitterTaskFactory = fileSplitterTaskFactory;
		this.logger = logger;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompletableFuture<Void> enqueue(final IFileWrapper fileWrapper) {
		List<CompletableFuture<Void>> futures = new ArrayList<>();
		futures.add(triggerReadingTask(fileWrapper));
		triggerSplittingTasks(fileWrapper, futures);
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
	}

	/**
	 * Trigger splitting tasks.
	 *
	 * @param fileWrapper the file wrapper
	 * @param futures the futures
	 */
	private void triggerSplittingTasks(final IFileWrapper fileWrapper, List<CompletableFuture<Void>> futures) {
		for (int i = 0; i < config.getConcurrencyLevel(); i++) {
			CompletableFuture<Void> completableFuture = CompletableFuture.<Void>supplyAsync(() -> {
				IFileTask<Void> writerTask = fileSplitterTaskFactory.createSplitter(fileWrapper);
				try {
					logger.log(Level.FINE,"Splitting file..."+ fileWrapper.getFile().getName());
					return writerTask.call();
				} catch (Exception e) {
					throw new CompletionException(e);
				}
			}, fileSplittingPool);
			futures.add(completableFuture);
		}
	}

	/**
	 * Trigger reading task.
	 *
	 * @param fileWrapper the file wrapper
	 * @return the completable future
	 */
	private CompletableFuture<Void> triggerReadingTask(final IFileWrapper fileWrapper) {
		return CompletableFuture.supplyAsync(() -> {
			IFileTask<Void> readerTask = fileReaderTaskFactory.createReader(fileWrapper);
			try {
				logger.log(Level.FINE,"Reading file..."+ fileWrapper.getFile().getName());
				return readerTask.call();
			} catch (Exception e) {
				throw new CompletionException(e);
			}
		}, fileSplittingPool);
	}

}
