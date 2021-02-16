package com.assignment.content_sorting.file.splitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Named;

import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.file.reader.FileReaderTask;
import com.assignment.content_sorting.file.reader.IFileReaderQueueHandler;
import com.assignment.content_sorting.file.reader.IFileWrapper;
import com.assignment.content_sorting.properties.IServerConfig;
import com.google.inject.Inject;

public class FileSplittingEnqueuer implements IFileProcessEnqueuer, IFileReaderQueueHandler {

	private final ExecutorService fileSplittingPool;
	private final BlockingQueue<String> lineQueue = new ArrayBlockingQueue<String>(100);
	private final AtomicBoolean isFileRead = new AtomicBoolean();
	private final ITempFileCache tempFileCache;
	private final IServerConfig config;

	@Inject
	public FileSplittingEnqueuer(@Named("FileSplittingExecutor") final ExecutorService fileSplittingPool,
			final ITempFileCache tempFileCache, final IServerConfig config) {
		this.fileSplittingPool = fileSplittingPool;
		this.tempFileCache = tempFileCache;
		this.config = config;
	}

	@Override
	public CompletableFuture<IFileWrapper> enqueue(final IFileWrapper fileWrapper) {
		CompletableFuture.supplyAsync(() -> {
			FileReaderTask readerTask = new FileReaderTask(fileWrapper, this);
			try {
				return readerTask.call();
			} catch (Exception e) {
				throw new CompletionException(e);
			}
		}, fileSplittingPool).whenComplete((result, ex) -> {
			this.isFileRead.getAndSet(result);
		});
		List<CompletableFuture<Void>> futures = new ArrayList<>();
		for (int i = 0; i < config.getConcurrencyLevel(); i++) {
			CompletableFuture<Void> completableFuture = CompletableFuture.<Void>supplyAsync(() -> {
				FileSplitterTask writerTask = new FileSplitterTask(this, tempFileCache, config);
				try {
					return writerTask.call();
				} catch (Exception e) {
					throw new CompletionException(e);
				}
			}, fileSplittingPool);
			futures.add(completableFuture);
		}
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).thenApply(obj -> {
			return fileWrapper;
		});
	}

	@Override
	public void addLinetoQueue(String line) throws InterruptedException {
		lineQueue.put(line);
	}

	@Override
	public boolean isFileRead() {
		return this.isFileRead.get();
	}

	@Override
	public String readLine() throws InterruptedException {
		return lineQueue.poll(50, TimeUnit.MILLISECONDS);
	}

	@Override
	public boolean isBufferEmpty() {
		return lineQueue.isEmpty();
	}

}
