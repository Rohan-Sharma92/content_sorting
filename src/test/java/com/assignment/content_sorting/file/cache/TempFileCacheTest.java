package com.assignment.content_sorting.file.cache;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.assignment.content_sorting.properties.IServerConfig;
import com.assignment.content_sorting.properties.ServerConfig;
import com.assignment.content_sorting.util.TestUtils;

@Test
public class TempFileCacheTest {

	public void testFileNamesAreSorted() {
		File file1 = Paths.get("target/temp").toFile();
		File file2 = Paths.get("target/abc").toFile();
		TempFileCache cache = new TempFileCache();
		cache.addTempFile("temp", file1);
		cache.addTempFile("abc", file2);
		List<String> tempFileNames = cache.getTempFileNames();
		Assert.assertEquals(tempFileNames.get(0), "abc");
		Assert.assertEquals(tempFileNames.get(1), "temp");
	}

	public void testSameCharacterFiles() {
		File file1 = Paths.get("target/a").toFile();
		File file2 = Paths.get("target/abc").toFile();
		TempFileCache cache = new TempFileCache();
		cache.addToPrefixCache(file1);
		cache.addToPrefixCache(file2);
		Set<File> fragmentedTempFiles = cache.getFragmentedTempFiles("a");
		Assert.assertTrue(fragmentedTempFiles.contains(file1));
		Assert.assertTrue(fragmentedTempFiles.contains(file2));
	}

	public void testCachePurge() {
		Map<String, String> props = new HashMap<String, String>();
		props.put(ServerConfig.TEMP, "target/temp");
		IServerConfig config = new ServerConfig(props);
		File file = TestUtils.writeTempFile("abc", "temp", config);
		TempFileCache cache = new TempFileCache();
		cache.addTempFile(file.getName(), file);
		cache.purgeTempFiles();
		File tempDir = Paths.get(config.getTempDirectory()).toFile();
		Assert.assertEquals(tempDir.listFiles().length, 0);
	}
}
