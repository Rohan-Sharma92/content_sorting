package com.assignment.content_sorting.file.splitter;

import java.io.File;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.assignment.content_sorting.exceptions.ContentSortingException;
import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.file.cache.TempFileCache;
import com.assignment.content_sorting.file.reader.FileReaderTask;
import com.assignment.content_sorting.file.reader.InputFileWrapper;
import com.assignment.content_sorting.mocks.MockValidationEngine;
import com.assignment.content_sorting.properties.IServerConfig;
import com.assignment.content_sorting.properties.ServerConfig;
import com.assignment.content_sorting.util.TestUtils;

@Test
public class FileSplitterTaskTest {

	private ITempFileCache tempFileCache;
	private IServerConfig config;

	@BeforeMethod
	public void setup() {
		tempFileCache = new TempFileCache();
		HashMap<String, String> properties = new HashMap<>();
		properties.put(ServerConfig.LISTEN, "target/test");
		properties.put(ServerConfig.TEMP, "target/temp");
		config = new ServerConfig(properties);
	}

	@AfterMethod
	public void afterTest() {
		TestUtils.clearDir(config.getListenDirectory());
		TestUtils.clearDir(config.getTempDirectory());
	}

	
	public void testFileSplit() throws Exception {
		File file = TestUtils.writeFile("abcdefghijklmnoprstuvxyz\n" + "abc\n" + "zyxut", "test",config);
		InputFileWrapper fileWrapper = new InputFileWrapper(file);
		FileReaderTask fileReaderTask = new FileReaderTask(fileWrapper,new MockValidationEngine<>());
		fileReaderTask.call();
		FileSplitterTask fileSplitterTask = new FileSplitterTask(fileWrapper, tempFileCache, config);
		fileSplitterTask.call();
		Assert.assertEquals(tempFileCache.getTempFileNames().size(),2);
		File f1 = tempFileCache.getTempFile("a");
		File f2 = tempFileCache.getTempFile("z");
		Assert.assertNotNull(f1);
		Assert.assertNotNull(f2);
	}
	
	public void testFileSplitWithReducedBatchSize() throws Exception {
		HashMap<String, String> properties = new HashMap<>();
		properties.put(ServerConfig.LISTEN,"target/test1");
		properties.put(ServerConfig.PROPERTY_MAX_BATCH_SIZE,"10");
		properties.put(ServerConfig.TEMP, "target/temp");
		config = new ServerConfig(properties);
		TestUtils.clearDir(config.getListenDirectory());		
		File file = TestUtils.writeFile("abcdefghijklmnoprstuvxyz\n" + "abc\n" + "zyxut", "test",config);
		InputFileWrapper fileWrapper = new InputFileWrapper(file);
		FileReaderTask fileReaderTask = new FileReaderTask(fileWrapper,new MockValidationEngine<>());
		fileReaderTask.call();
		FileSplitterTask fileSplitterTask = new FileSplitterTask(fileWrapper, tempFileCache, config);
		fileSplitterTask.call();
		Assert.assertEquals(tempFileCache.getTempFileNames().size(),3);
		File f1 = tempFileCache.getTempFile("a");
		File f2 = tempFileCache.getTempFile("ab");
		File f3 = tempFileCache.getTempFile("z");
		Assert.assertNotNull(f1);
		Assert.assertNotNull(f2);
		Assert.assertNotNull(f3);
	}
	
	public void testExceptionWhenNonAlphanumericContentFound() {
		try {
		File file = TestUtils.writeFile("?\n" + "abc\n" + "zyxut", "test",config);
		InputFileWrapper fileWrapper = new InputFileWrapper(file);
		MockValidationEngine<String> validationEngine = new MockValidationEngine<>();
		validationEngine.setValidationFailure();
		FileReaderTask fileReaderTask = new FileReaderTask(fileWrapper, validationEngine);
		fileReaderTask.call();
		FileSplitterTask fileSplitterTask = new FileSplitterTask(fileWrapper, tempFileCache, config);
		fileSplitterTask.call();
		}
		catch(Exception e) {
			Assert.assertTrue(e instanceof ContentSortingException);
			Assert.assertEquals(e.getMessage(),"Invalid line. Only alphanumeric characters allowed");
		}
	}
}
