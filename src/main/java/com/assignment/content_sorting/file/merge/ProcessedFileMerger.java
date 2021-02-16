package com.assignment.content_sorting.file.merge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.properties.IServerConfig;
import com.assignment.content_sorting.service.IContentProcessor;
import com.google.inject.Inject;

public class ProcessedFileMerger implements IContentProcessor {

	private static final String OUTPUT_TXT = "output.txt";
	private final ITempFileCache tempFileCache;
	private final IServerConfig serverConfig;

	@Inject
	public ProcessedFileMerger(final ITempFileCache tempFileCache, final IServerConfig serverConfig) {
		this.tempFileCache = tempFileCache;
		this.serverConfig = serverConfig;
	}

	@Override
	public CompletableFuture<Void> process() {
		File outputDir = Paths.get(serverConfig.getOutputDirectory()).toFile();
		if (!outputDir.exists())
			outputDir.mkdirs();
		try {
			Arrays.stream(outputDir.listFiles()).forEach(file -> file.delete());
			File outputFile = new File(outputDir, OUTPUT_TXT);
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile, false));
			for (String fileName : tempFileCache.getTempFileNames()) {
				File tempFile = tempFileCache.getTempFile(fileName);
				if (!tempFile.exists())
					continue;
				BufferedReader br = Files.newBufferedReader(tempFile.toPath());
				String line = null;
				while ((line = br.readLine()) != null) {
					bufferedWriter.write(line);
					bufferedWriter.newLine();
				}
				bufferedWriter.flush();
				br.close();
			}
			bufferedWriter.close();
			return CompletableFuture.completedFuture(null);
		} catch (IOException exception) {

		}
		return null;
	}

}
