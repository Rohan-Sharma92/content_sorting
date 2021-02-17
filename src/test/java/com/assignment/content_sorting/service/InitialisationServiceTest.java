package com.assignment.content_sorting.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.assignment.content_sorting.mocks.MockService;
import com.assignment.content_sorting.properties.ServerConfig;

@Test
public class InitialisationServiceTest {

	private ServerConfig config;

	@BeforeMethod
	public void setup() {
		Map<String, String> properties = new HashMap<>();
		properties.put(ServerConfig.LISTEN, "target/test");
		properties.put(ServerConfig.TEMP, "target/temp");
		properties.put(ServerConfig.OUTPUT, "target/output");
		config = new ServerConfig(properties);
	}
	@AfterMethod
	public void afterTest() {
		clearDir(config.getListenDirectory());
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


	public void testDirectoriesAreInitialized() {
		InitialisationService initialisationService = new InitialisationService(config);
		MockService dependentService = new MockService();
		initialisationService.addDependencies(dependentService);
		initialisationService.start();
		File listen = Paths.get(config.getListenDirectory()).toFile();
		Assert.assertTrue(listen.exists());
		File temp = Paths.get(config.getTempDirectory()).toFile();
		Assert.assertTrue(temp.exists());
		File out = Paths.get(config.getOutputDirectory()).toFile();
		Assert.assertTrue(out.exists());
		Assert.assertTrue(dependentService.isStarted());
	}
	
	public void testDirectoriesAreCleanedUponInitialization() throws IOException {
		File temp = Paths.get(config.getTempDirectory()).toFile();
		if(!temp.exists()) {
			temp.mkdirs();
		}
		File.createTempFile("test", "txt", temp);
		File out = Paths.get(config.getOutputDirectory()).toFile();
		if(!out.exists()) {
			out.mkdirs();
		}
		File.createTempFile("test", "txt", out);
		InitialisationService initialisationService = new InitialisationService(config);
		initialisationService.start();
		File listen = Paths.get(config.getListenDirectory()).toFile();
		Assert.assertTrue(listen.exists());
		Assert.assertTrue(temp.exists());
		Assert.assertTrue(out.exists());
		Assert.assertEquals(temp.listFiles().length,0);
		Assert.assertEquals(out.listFiles().length,0);
	}
}
