package com.assignment.content_sorting.file.splitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.file.cache.TempFileCache;
import com.assignment.content_sorting.file.reader.FileReaderTask;
import com.assignment.content_sorting.file.reader.InputFileWrapper;
import com.assignment.content_sorting.properties.IServerConfig;
import com.assignment.content_sorting.properties.ServerConfig;

@Test
public class FileSplitterTaskTest {

	private ITempFileCache tempFileCache;
	private IServerConfig config;

	@BeforeMethod
	public void setup() {
		tempFileCache = new TempFileCache();
		HashMap<String, String> properties = new HashMap<>();
		properties.put(ServerConfig.LISTEN,"test");
		config = new ServerConfig(properties);
	}

	@AfterMethod
	public void afterTest() {
		clearDir(config.getListenDirectory());
		clearDir(config.getTempDirectory());
	}

	private void clearDir(String dirName) {
		File dir = Paths.get(dirName).toFile();
		if (dir.exists()) {
			for (File f : dir.listFiles()) {
				f.delete();
			}
		}
	}

	private File writeFile(String content, String fileName) {
		String tempFilesDir = config.getListenDirectory();
		File dir = Paths.get(config.getListenDirectory()).toFile();
		if(!dir.exists()) {
			dir.mkdirs();
		}
		File file = Paths.get(tempFilesDir, fileName + ".txt").toFile();
		try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(file, false))) {

			bw.write(content);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return file;
	}

	public void testFileSplit() throws Exception {
		File file = writeFile("abcdefghijklmnoprstuvxyz\n" + "abc\n" + "zyxut", "test");
		InputFileWrapper fileWrapper = new InputFileWrapper(file);
		FileReaderTask fileReaderTask = new FileReaderTask(fileWrapper);
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
		properties.put(ServerConfig.LISTEN,"test");
		properties.put(ServerConfig.PROPERTY_MAX_BATCH_SIZE,"10");
		config = new ServerConfig(properties);
		File file = writeFile("abcdefghijklmnoprstuvxyz\n" + "abc\n" + "zyxut", "test");
		InputFileWrapper fileWrapper = new InputFileWrapper(file);
		FileReaderTask fileReaderTask = new FileReaderTask(fileWrapper);
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
}
