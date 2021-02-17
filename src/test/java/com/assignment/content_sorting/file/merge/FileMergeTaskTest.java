package com.assignment.content_sorting.file.merge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.assignment.content_sorting.file.cache.TempFileCache;
import com.assignment.content_sorting.properties.ServerConfig;
import com.assignment.content_sorting.util.TestUtils;

@Test
public class FileMergeTaskTest {

	private FileMergeTask fileMergeTask;
	private TempFileCache tempFileCache;
	private ServerConfig config;

	@BeforeMethod
	public void setup() {
		tempFileCache = new TempFileCache();
		HashMap<String, String> properties = new HashMap<>();
		properties.put(ServerConfig.TEMP, "target/test");
		config = new ServerConfig(properties);
	}

	@AfterMethod
	public void afterTest() {
		TestUtils.clearDir(config.getTempDirectory());
	}

	public void testMergeMultipleFiles() throws Exception {
		File file1 = TestUtils.writeTempFile("abc\n" + "acf\n" + "aght","ab",config);
		File file2 = TestUtils.writeTempFile("accde\n" + "ahbnh\n" + "azghg","ac",config);
		File file3 = TestUtils.writeTempFile("a\n" + "afgh","a",config);
		tempFileCache.addTempFile("a",file3);
		tempFileCache.addTempFile("ab",file1);
		tempFileCache.addTempFile("ac",file2);
		tempFileCache.addToPrefixCache(file1);
		tempFileCache.addToPrefixCache(file2);
		tempFileCache.addToPrefixCache(file3);
		fileMergeTask = new FileMergeTask("a", tempFileCache.getFragmentedTempFiles("a"), config);
		fileMergeTask.call();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file3));
		LinkedList<String> data = new LinkedList<>();
		String line = null;
		while((line=bufferedReader.readLine())!=null) {
			data.add(line);
		}
		bufferedReader.close();
		Assert.assertEquals(data.size(),8);
		Iterator<String> iterator = data.iterator();
		Assert.assertEquals(iterator.next(),"a");
		Assert.assertEquals(iterator.next(),"abc");
		Assert.assertEquals(iterator.next(),"accde");
		Assert.assertEquals(iterator.next(),"acf");
		Assert.assertEquals(iterator.next(),"afgh");
		Assert.assertEquals(iterator.next(),"aght");
		Assert.assertEquals(iterator.next(),"ahbnh");
		Assert.assertEquals(iterator.next(),"azghg");
		Assert.assertFalse(iterator.hasNext());
	}

}
