package com.assignment.content_sorting.file.factories;

import java.io.File;
import java.util.Set;

import com.assignment.content_sorting.file.common.IFileTask;

public interface IFileMergeTaskFactory {

	IFileTask<Void> createMergeTask(final String fileName, final Set<File> fragments);
	
}
