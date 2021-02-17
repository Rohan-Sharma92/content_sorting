package com.assignment.content_sorting.service;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.assignment.content_sorting.file.cache.TempFileCache;
import com.assignment.content_sorting.mocks.MockContentProcessor;

@Test
public class ContentSortingServiceTest {

	private ContentSortingService contentSortingService;

	public void testSequence() {
		MockContentProcessor fileSplitter= new MockContentProcessor("FileSplitter");
		MockContentProcessor fileSorter= new MockContentProcessor("FileSorter");
		MockContentProcessor fileMerger= new MockContentProcessor("TempFileMerge");
		MockContentProcessor processedFileMerger= new MockContentProcessor("ProcessedFileMerge");
		TempFileCache tempFileCache = new TempFileCache();
		contentSortingService = new ContentSortingService(fileSplitter, fileSorter, fileMerger, processedFileMerger,
				tempFileCache);
		contentSortingService.start();
		Assert.assertTrue(fileSplitter.isProgress());
		Assert.assertFalse(fileSorter.isProgress());
		Assert.assertFalse(fileMerger.isProgress());
		Assert.assertFalse(processedFileMerger.isProgress());
		fileSplitter.markComplete();
		Assert.assertTrue(fileSplitter.isProcessed());
		Assert.assertTrue(fileSorter.isProgress());
		Assert.assertFalse(fileSorter.isProcessed());
		Assert.assertFalse(fileMerger.isProgress());
		Assert.assertFalse(processedFileMerger.isProgress());
		fileSorter.markComplete();
		Assert.assertTrue(fileSorter.isProcessed());
		Assert.assertTrue(fileMerger.isProgress());
		Assert.assertFalse(fileMerger.isProcessed());
		Assert.assertFalse(processedFileMerger.isProgress());
		fileMerger.markComplete();
		Assert.assertTrue(fileMerger.isProcessed());
		Assert.assertTrue(processedFileMerger.isProgress());
		Assert.assertFalse(processedFileMerger.isProcessed());
		processedFileMerger.markComplete();
		Assert.assertTrue(processedFileMerger.isProcessed());
	}
}
