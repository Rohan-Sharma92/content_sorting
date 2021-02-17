package com.assignment.content_sorting.file.reader;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.assignment.content_sorting.mocks.MockValidationEngine;
import com.assignment.content_sorting.properties.ServerConfig;
import com.assignment.content_sorting.util.TestUtils;

@Test
public class FileReaderTaskTest {

	private FileReaderTask fileReaderTask;
	
	public void testFileIsMarkedComplete() throws Exception {
		HashMap<String, String> properties = new HashMap<>();
		properties.put(ServerConfig.LISTEN, "target/test");
		ServerConfig config = new ServerConfig(properties);
		IFileWrapper fileWrapper= new InputFileWrapper(TestUtils.writeFile("abc\n"+"bdfg", "test",config));
		fileReaderTask = new FileReaderTask(fileWrapper,new MockValidationEngine<>());
		fileReaderTask.call();
		Assert.assertTrue(fileWrapper.isFileRead());
	}
	
	
}
