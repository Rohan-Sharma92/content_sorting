package com.assignment.content_sorting.file.merge;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

import javax.inject.Named;

import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.file.factories.IFileMergeTaskFactory;
import com.assignment.content_sorting.service.IContentProcessor;
import com.google.inject.Inject;

/**
 * The Class TempFileMerger.
 * @author Rohan
 */
public class TempFileMerger implements IContentProcessor{

	/** The temp file cache. */
	private final ITempFileCache tempFileCache;
	
	/** The merge pool. */
	private final ExecutorService mergePool;
	
	/** The merge task factory. */
	private final IFileMergeTaskFactory mergeTaskFactory;

	/**
	 * Instantiates a new temp file merger.
	 *
	 * @param tempFileCache the temp file cache
	 * @param mergePool the merge pool
	 * @param mergeTaskFactory the merge task factory
	 */
	@Inject
	public TempFileMerger(final ITempFileCache tempFileCache,
			final @Named("ContentSortingExecutor") ExecutorService mergePool,
			final IFileMergeTaskFactory mergeTaskFactory) {
		this.tempFileCache = tempFileCache;
		this.mergePool=mergePool;
		this.mergeTaskFactory = mergeTaskFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompletableFuture<Void> process() {
		List<CompletableFuture<Void>> futures = new ArrayList<>();
		for(String fileName: tempFileCache.getTempFileNames()) {
			Set<File> fragments = tempFileCache.getFragmentedTempFiles(fileName);
			if(fragments!=null) {
				if(fragments.size()>1) {
					futures.add(CompletableFuture.supplyAsync(()->{
						try {
							return mergeTaskFactory.createMergeTask(fileName, fragments).call();
						} catch (Exception e) {
							throw new CompletionException(e);
						}
					},mergePool));
				}
			}
		}
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{}));
	}
}
