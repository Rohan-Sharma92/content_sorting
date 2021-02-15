package com.assignment.content_sorting.file.splitter;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;

import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.file.writer.FileWriterTask;
import com.assignment.content_sorting.properties.IServerConfig;

public class FileSplitterTask extends FileWriterTask implements Callable<Void> {

	private static final String TXT = ".txt";
	private final IFileProcessEnqueuer fileProcessEnqueuer;
	private final ITempFileCache tempFileCache;
	private final IServerConfig config;

	public FileSplitterTask(final IFileProcessEnqueuer fileProcessEnqueuer, final ITempFileCache tempFileCache,
			final IServerConfig config) {
		this.fileProcessEnqueuer = fileProcessEnqueuer;
		this.tempFileCache = tempFileCache;
		this.config = config;
	}

	@Override
	public Void call() throws Exception {
		while (!fileProcessEnqueuer.isFileRead()
				|| (fileProcessEnqueuer.isFileRead() && !fileProcessEnqueuer.isBufferEmpty())) {
			String line = fileProcessEnqueuer.readLine();
			if (line != null) {
				process(line);
			}
		}
		return null;
	}

	private void process(String line) {
		if (StringUtils.isAllBlank(line) || StringUtils.isAllEmpty(line))
			return;
		File file = getFile(line);
		writeLines(Arrays.asList(line), file, true);
	}

	private File getFile(String line) {
		return getFile(StringUtils.EMPTY, line);
	}

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
