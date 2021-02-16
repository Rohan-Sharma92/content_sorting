package com.assignment.content_sorting.file.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.assignment.content_sorting.properties.IServerConfig;
import com.assignment.content_sorting.properties.ServerConfig;

@Test
public class FileReaderTaskTest {

	private FileReaderTask fileReaderTask;
	
	public void testFileIsMarkedComplete() throws Exception {
		IFileWrapper fileWrapper= new InputFileWrapper(writeFile("abc\n"+"bdfg", "test"));
		fileReaderTask = new FileReaderTask(fileWrapper);
		Boolean result = fileReaderTask.call();
		Assert.assertTrue(result);
		Assert.assertTrue(fileWrapper.isFileRead());
	}
	
	private File writeFile(String content, String fileName) {
		IServerConfig config = new ServerConfig();
		String tempFilesDir = config.getTempDirectory();
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
