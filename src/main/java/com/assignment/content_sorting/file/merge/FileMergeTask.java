package com.assignment.content_sorting.file.merge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import com.assignment.content_sorting.file.common.IFileTask;
import com.assignment.content_sorting.file.writer.FileWriterTask;
import com.assignment.content_sorting.properties.IServerConfig;
import com.assignment.content_sorting.util.BufferedReaderWrapper;
import com.assignment.content_sorting.util.SortedList;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class FileMergeTask implements IFileTask<Void> {

	private static final String TXT = ".txt";
	private static final String K_WAY_DIR = "k-way";
	private final String fileName;
	private final Set<File> fragments;
	private final FileWriterTask fileWriter;
	private final IServerConfig config;

	@Inject
	public FileMergeTask(@Assisted final String fileName, @Assisted final Set<File> fragments,
			final IServerConfig config) {
		this.fileName = fileName;
		this.fragments = fragments;
		this.config = config;
		this.fileWriter = new FileWriterTask();
	}

	@Override
	public Void call() throws Exception {
		executeKMerge();
		return null;
	}

	private void executeKMerge() throws IOException {
		File tempDir = Paths.get(config.getTempDirectory() + K_WAY_DIR).toFile();
		if (!tempDir.exists()) {
			tempDir.mkdirs();
		}

		File outputFile = new File(tempDir, fileName);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
		SortedList<BufferedReaderWrapper> sortedList = getAutoSortedList();
		mergeSortedFiles(sortedList, bufferedWriter);
		bufferedWriter.flush();
		fileWriter.delete(fragments);
		bufferedWriter.close();
		File destination = Paths.get(config.getTempDirectory(), fileName + TXT).toFile();
		fileWriter.move(outputFile, destination);
		tempDir.delete();
	}

	private void mergeSortedFiles(SortedList<BufferedReaderWrapper> sortedList, BufferedWriter bufferedWriter)
			throws IOException {
		int rowCount = 0;
		while (!sortedList.isEmpty()) {
			BufferedReaderWrapper readerWrapper = sortedList.poll();
			bufferedWriter.append(readerWrapper.next());
			bufferedWriter.newLine();
			rowCount++;
			if (!readerWrapper.isEmpty()) {
				sortedList.add(readerWrapper);
			} else {
				readerWrapper.close();
			}
			if (rowCount % 1000 == 0) {
				bufferedWriter.flush();
			}
		}
	}

	private SortedList<BufferedReaderWrapper> getAutoSortedList() throws IOException {
		SortedList<BufferedReaderWrapper> sortedList = new SortedList<>();
		for (File file : fragments) {
			BufferedReaderWrapper reader = new BufferedReaderWrapper(
					Files.newBufferedReader(Paths.get(file.getAbsolutePath())));
			sortedList.add(reader);
		}
		return sortedList;
	}

}
