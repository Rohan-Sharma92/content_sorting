package com.assignment.content_sorting.file.splitter;

import java.io.File;
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

public class FileSplitterTask extends FileWriterTask implements IFileTask<Void> {

	private static final String TXT = ".txt";
	private final ITempFileCache tempFileCache;
	private final IServerConfig config;
	private final IFileWrapper fileWrapper;

	@Inject
	public FileSplitterTask(@Assisted final IFileWrapper fileWrapper, final ITempFileCache tempFileCache,
			final IServerConfig config) {
		this.tempFileCache = tempFileCache;
		this.config = config;
		this.fileWrapper = fileWrapper;
	}

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
