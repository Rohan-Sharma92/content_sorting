package com.assignment.content_sorting.file.splitter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.file.common.IFileTask;
import com.assignment.content_sorting.file.reader.IFileWrapper;
import com.assignment.content_sorting.file.writer.FileWriterTask;
import com.assignment.content_sorting.properties.IServerConfig;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * The Class FileSplitterTask.
 */
public class FileSplitterTask extends FileWriterTask implements IFileTask<Void> {

	/** The Constant TXT. */
	private static final String TXT = ".txt";
	
	/** The temp file cache. */
	private final ITempFileCache tempFileCache;
	
	/** The config. */
	private final IServerConfig config;
	
	/** The file wrapper. */
	private final IFileWrapper fileWrapper;

	/**
	 * Instantiates a new file splitter task.
	 *
	 * @param fileWrapper the file wrapper
	 * @param tempFileCache the temp file cache
	 * @param config the config
	 */
	@Inject
	public FileSplitterTask(@Assisted final IFileWrapper fileWrapper, final ITempFileCache tempFileCache,
			final IServerConfig config) {
		this.tempFileCache = tempFileCache;
		this.config = config;
		this.fileWrapper = fileWrapper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void call() throws Exception {
		while (!fileWrapper.isFileRead() || (fileWrapper.isFileRead() && !fileWrapper.isBufferEmpty())) {
			String line = fileWrapper.readLine();
			if (line != null) {
				process(line);
			}
		}
		return null;
	}

	/**
	 * Process.
	 *
	 * @param line the line
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void process(String line) throws IOException {
		if (StringUtils.isAllBlank(line) || StringUtils.isAllEmpty(line))
			return;
		File file = getFile(line);
		writeLines(Arrays.asList(line), file, true);
	}

	/**
	 * Gets the file.
	 *
	 * @param line the line
	 * @return the file
	 */
	private File getFile(String line) {
		return getFile(StringUtils.EMPTY, line);
	}

	/**
	 * Gets the file.
	 *
	 * @param prefix the prefix
	 * @param line the line
	 * @return the file
	 */
	protected File getFile(String prefix, String line) {
		String filename = (prefix + line.charAt(0)).toLowerCase();
		File file = this.tempFileCache.getTempFile(filename);
		if (file == null) {
			file = Paths.get(config.getTempDirectory(), filename + TXT).toFile();
			file.deleteOnExit();
			tempFileCache.addTempFile(filename, file);
		} else {
			if (tempFileCache.isFileComplete(filename) || file.length() >= config.getMaxBatchSize()) {
				tempFileCache.addCompletedFile(filename);
				file = getFile(filename, line.substring(1));
			}
		}
		tempFileCache.addToPrefixCache(file);
		return file;
	}

}
