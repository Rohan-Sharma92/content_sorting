package com.assignment.content_sorting.file.splitter;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.assignment.content_sorting.mocks.MockFileProcessEnqueuer;
import com.assignment.content_sorting.mocks.MockFileSplittingEnqueuerFactory;
import com.assignment.content_sorting.properties.ServerConfig;
import com.assignment.content_sorting.util.TestUtils;

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
		fileSplitter = new FileSplitter(config, fileSplittingEnqueuerFactory,Logger.getLogger(""));
	}

	@AfterMethod
	public void afterTest() {
		TestUtils.clearDir(config.getListenDirectory());
		TestUtils.clearDir(config.getTempDirectory());
	}


	@Test
	public void testSplitIsTriggeredForAllFiles() {
		File file1 = TestUtils.writeFile("abcdefghijklmnoprstuvxyz\n" + "abc\n" + "zyxut", "test1",config);
		MockFileProcessEnqueuer enqueuer1 = new MockFileProcessEnqueuer();
		File file2 = TestUtils.writeFile("abdc\n" + "hgfs", "test2",config);
		MockFileProcessEnqueuer enqueuer2 = new MockFileProcessEnqueuer();
		fileSplittingEnqueuerFactory.setExpectedEnqueuer(Arrays.asList(enqueuer2, enqueuer1));
		CompletableFuture<Void> completableFuture = fileSplitter.process();
		Assert.assertTrue(completableFuture.isDone());
		Assert.assertEquals(enqueuer2.getFile().getFile(), file1);
		Assert.assertEquals(enqueuer1.getFile().getFile(), file2);
	}
}
