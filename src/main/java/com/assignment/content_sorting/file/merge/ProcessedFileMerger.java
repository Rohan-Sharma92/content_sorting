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

import org.apache.commons.lang3.RandomStringUtils;

import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.file.writer.FileWriterTask;
import com.assignment.content_sorting.properties.IServerConfig;
import com.assignment.content_sorting.service.IContentProcessor;
import com.google.inject.Inject;

/**
 * The Class ProcessedFileMerger.
 * 
 * @author Rohan
 */
public class ProcessedFileMerger implements IContentProcessor {

	/** The Constant OUTPUT_TXT. */
	public static final String OUTPUT_TXT = "/output.txt";

	/** The temp file cache. */
	private final ITempFileCache tempFileCache;

	/** The server config. */
	private final IServerConfig serverConfig;

	/**
	 * Instantiates a new processed file merger.
	 *
	 * @param tempFileCache the temp file cache
	 * @param serverConfig  the server config
	 */
	@Inject
	public ProcessedFileMerger(final ITempFileCache tempFileCache, final IServerConfig serverConfig) {
		this.tempFileCache = tempFileCache;
		this.serverConfig = serverConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.assignment.content_sorting.service.IContentProcessor#process()
	 */
	@Override
	public CompletableFuture<Void> process() {
		File outputDir = Paths.get(serverConfig.getOutputDirectory()).toFile();
		if (!outputDir.exists())
			outputDir.mkdirs();
		try {
			Arrays.stream(outputDir.listFiles()).forEach(file -> file.delete());
			File outputFile = new File(serverConfig.getTempDirectory(),
					RandomStringUtils.randomAlphanumeric(4)+".txt");
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
			File destination = Paths.get(outputDir + OUTPUT_TXT).toFile();
			new FileWriterTask().move(outputFile, destination);
			return CompletableFuture.completedFuture(null);
		} catch (IOException exception) {

		}
		return null;
	}

}
