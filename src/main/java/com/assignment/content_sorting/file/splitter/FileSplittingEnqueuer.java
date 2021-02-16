package com.assignment.content_sorting.file.splitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

import javax.inject.Named;

import com.assignment.content_sorting.file.common.IFileTask;
import com.assignment.content_sorting.file.factories.IFileReaderTaskFactory;
import com.assignment.content_sorting.file.factories.IFileSplitterTaskFactory;
import com.assignment.content_sorting.file.reader.IFileWrapper;
import com.assignment.content_sorting.properties.IServerConfig;
import com.google.inject.Inject;

public class FileSplittingEnqueuer implements IFileProcessEnqueuer {

	private final ExecutorService fileSplittingPool;
	private final IServerConfig config;
	private final IFileSplitterTaskFactory fileSplitterTaskFactory;
	private final IFileReaderTaskFactory fileReaderTaskFactory;

	@Inject
	public FileSplittingEnqueuer(@Named("FileSplittingExecutor") final ExecutorService fileSplittingPool,
			final IServerConfig config, final IFileReaderTaskFactory fileReaderTaskFactory,
			final IFileSplitterTaskFactory fileSplitterTaskFactory) {
		this.fileSplittingPool = fileSplittingPool;
		this.config = config;
		this.fileReaderTaskFactory = fileReaderTaskFactory;
		this.fileSplitterTaskFactory = fileSplitterTaskFactory;
	}

	@Override
	public CompletableFuture<IFileWrapper> enqueue(final IFileWrapper fileWrapper) {
		triggerReadingTask(fileWrapper);
		List<CompletableFuture<Void>> futures = new ArrayList<>();
		triggerSplittingTasks(fileWrapper, futures);
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).thenApply(obj -> {
			return fileWrapper;
		});
	}

	private void triggerSplittingTasks(final IFileWrapper fileWrapper, List<CompletableFuture<Void>> futures) {
		for (int i = 0; i < config.getConcurrencyLevel(); i++) {
			CompletableFuture<Void> completableFuture = CompletableFuture.<Void>supplyAsync(() -> {
				IFileTask<Void> writerTask = fileSplitterTaskFactory.createSplitter(fileWrapper);
				try {
					return writerTask.call();
				} catch (Exception e) {
					throw new CompletionException(e);
				}
			}, fileSplittingPool);
			futures.add(completableFuture);
		}
	}

	private void triggerReadingTask(final IFileWrapper fileWrapper) {
		CompletableFuture.supplyAsync(() -> {
			IFileTask<Boolean> readerTask = fileReaderTaskFactory.createReader(fileWrapper);
			try {
				return readerTask.call();
			} catch (Exception e) {
				throw new CompletionException(e);
			}
		}, fileSplittingPool);
	}

}
