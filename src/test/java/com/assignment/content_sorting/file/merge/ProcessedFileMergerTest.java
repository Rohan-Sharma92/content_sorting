package com.assignment.content_sorting.file.merge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.file.cache.TempFileCache;
import com.assignment.content_sorting.properties.IServerConfig;
import com.assignment.content_sorting.properties.ServerConfig;

@Test
public class ProcessedFileMergerTest {

	private ProcessedFileMerger fileMerger;
	private IServerConfig config;
	private ITempFileCache tempFileCache;

	@BeforeMethod
	public void setup() {
		tempFileCache = new TempFileCache();
		Map<String, String> props = new HashMap<>();
		props.put(ServerConfig.OUTPUT, "target/output");
		props.put(ServerConfig.TEMP, "target/test");
		config = new ServerConfig(props);
		fileMerger = new ProcessedFileMerger(tempFileCache, config);
	}

	@AfterMethod
	public void afterTest() {
		clearDir(config.getTempDirectory());
		clearDir(config.getOutputDirectory());
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
		String tempFilesDir = config.getTempDirectory();
		File dir = Paths.get(config.getTempDirectory()).toFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File outputdir = Paths.get(config.getOutputDirectory()).toFile();
		if (!outputdir.exists()) {
			outputdir.mkdirs();
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

	public void testMergeMultipleFiles() throws Exception {
		File file1 = writeFile("abc\n" + "acf\n" + "aght", "a");
		File file2 = writeFile("bdf\n" + "bhn\n" + "bzg", "b");
		tempFileCache.addTempFile(file1.getName(), file1);
		tempFileCache.addTempFile(file2.getName(), file2);
		fileMerger.process();
		BufferedReader bufferedReader = Files
				.newBufferedReader(Paths.get(config.getOutputDirectory() + "/" + ProcessedFileMerger.OUTPUT_TXT));
		LinkedList<String> data = new LinkedList<>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			data.add(line);
		}
		bufferedReader.close();
		Assert.assertEquals(data.size(), 6);
		Iterator<String> iterator = data.iterator();
		Assert.assertEquals(iterator.next(), "abc");
		Assert.assertEquals(iterator.next(), "acf");
		Assert.assertEquals(iterator.next(), "aght");
		Assert.assertEquals(iterator.next(), "bdf");
		Assert.assertEquals(iterator.next(), "bhn");
		Assert.assertEquals(iterator.next(), "bzg");
		Assert.assertFalse(iterator.hasNext());
	}
}
