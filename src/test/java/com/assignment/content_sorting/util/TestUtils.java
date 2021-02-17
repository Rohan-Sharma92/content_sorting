package com.assignment.content_sorting.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.assignment.content_sorting.properties.IServerConfig;

public class TestUtils {
	public static void clearDir(String dirName) {
		File dir = Paths.get(dirName).toFile();
		if (dir.exists()) {
			for (File f : dir.listFiles()) {
				f.delete();
			}
		}
	}

	public static File writeFile(String content, String fileName,IServerConfig config) {
		String tempFilesDir = config.getListenDirectory();
		return create(content, fileName, config, tempFilesDir);
	}

	private static File create(String content, String fileName, IServerConfig config, String tempFilesDir) {
		File dir = Paths.get(config.getListenDirectory()).toFile();
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		File tempDir = Paths.get(config.getTempDirectory()).toFile();
		if(!tempDir.exists()) {
			tempDir.mkdirs();
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
	
	public static File writeTempFile(String content, String fileName,IServerConfig config) {
		String tempFilesDir = config.getTempDirectory();
		return create(content, fileName, config, tempFilesDir);
	}

}
