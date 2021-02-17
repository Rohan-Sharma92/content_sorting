package com.assignment.content_sorting.file.sorter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.assignment.content_sorting.file.common.IFileTask;
import com.assignment.content_sorting.file.factories.IFileReaderTaskFactory;
import com.assignment.content_sorting.file.reader.FileReaderTask;
import com.assignment.content_sorting.file.sort.FileSorterTask;
import com.assignment.content_sorting.mocks.MockValidationEngine;
import com.assignment.content_sorting.properties.ServerConfig;
import com.assignment.content_sorting.validation.engine.IValidationEngine;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;

@Test
public class FileSorterTaskTest {

	private FileSorterTask sorterTask;
	private ServerConfig config;
	private IFileReaderTaskFactory readerTaskFactory;
	private MockValidationEngine<String> validationEngine;

	@BeforeMethod
	public void setup() {
		HashMap<String, String> properties = new HashMap<>();
		properties.put(ServerConfig.LISTEN, "target/test");
		config = new ServerConfig(properties);
		validationEngine = new MockValidationEngine<>();
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				install(new FactoryModuleBuilder().implement(new TypeLiteral<IFileTask<Void>>() {
				}, FileReaderTask.class).build(IFileReaderTaskFactory.class));
				bind(new TypeLiteral<IValidationEngine<String>>() {
				}).toInstance(validationEngine);
			}
		});
		readerTaskFactory = injector.getInstance(IFileReaderTaskFactory.class);
		clearDir(config.getListenDirectory());
		clearDir(config.getTempDirectory());
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
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = Paths.get(tempFilesDir, fileName + ".txt").toFile();
		try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(file, false))) {

			bw.write(content);
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return file;
	}

	public void testSorter() throws Exception {
		File file = writeFile("abcdefghijklmnoprstuvxyz\n" + "abc\n" + "zyxut", "test");
		sorterTask = new FileSorterTask(file, readerTaskFactory);
		sorterTask.call();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String line = bufferedReader.readLine();
		Assert.assertEquals(line, "abc");
		line = bufferedReader.readLine();
		Assert.assertEquals(line, "abcdefghijklmnoprstuvxyz");
		line = bufferedReader.readLine();
		Assert.assertEquals(line, "zyxut");
		bufferedReader.close();
	}
}
