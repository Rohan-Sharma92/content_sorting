package com.assignment.content_sorting.file.factories;

import java.io.File;
import java.util.Set;

import com.assignment.content_sorting.file.common.IFileTask;

/**
 * A factory for creating IFileMergeTask objects.
 */
public interface IFileMergeTaskFactory {

	/**
	 * Creates a new IFileMergeTask object.
	 *
	 * @param fileName the file name
	 * @param fragments the fragments
	 * @return the i file task< void>
	 */
	IFileTask<Void> createMergeTask(final String fileName, final Set<File> fragments);
	
}
