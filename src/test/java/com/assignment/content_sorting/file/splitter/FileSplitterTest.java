package com.assignment.content_sorting.file.splitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.assignment.content_sorting.mocks.MockFileProcessEnqueuer;
import com.assignment.content_sorting.mocks.MockFileSplittingEnqueuerFactory;
import com.assignment.content_sorting.properties.ServerConfig;

@Test
public class FileSplitterTest {

	private FileSplitter fileSplitter;
	private ServerConfig config;
	private MockFileSplittingEnqueuerFactory fileSplittingEnqueuerFactory;

	@BeforeMethod
	public void setup() {
		HashMap<String, String> properties = new HashMap<>();
		properties.put(ServerConfig.LISTEN, "target/test");
		properties.put(ServerConfig.TEMP, "target/temp");
		config = new ServerConfig(properties);
		fileSplittingEnqueuerFactory = new MockFileSplittingEnqueuerFactory();
		fileSplitter = new FileSplitter(config, fileSplittingEnqueuerFactory);
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

	@Test
	public void testSplitIsTriggeredForAllFiles() {
		File file1 = writeFile("abcdefghijklmnoprstuvxyz\n" + "abc\n" + "zyxut", "test1");
		MockFileProcessEnqueuer enqueuer1 = new MockFileProcessEnqueuer();
		File file2 = writeFile("abdc\n" + "hgfs", "test2");
		MockFileProcessEnqueuer enqueuer2 = new MockFileProcessEnqueuer();
		fileSplittingEnqueuerFactory.setExpectedEnqueuer(Arrays.asList(enqueuer2, enqueuer1));
		CompletableFuture<Void> completableFuture = fileSplitter.process();
		Assert.assertTrue(completableFuture.isDone());
		Assert.assertEquals(enqueuer2.getFile().getFile(), file1);
		Assert.assertEquals(enqueuer1.getFile().getFile(), file2);
	}

	private File writeFile(String content, String fileName) {
		String tempFilesDir = config.getListenDirectory();
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
}
