package com.assignment.content_sorting.file.factories;

import java.io.File;

import com.assignment.content_sorting.file.common.IFileTask;
import com.google.inject.assistedinject.Assisted;

/**
 * A factory for creating IFileSorterTask objects.
 */
public interface IFileSorterTaskFactory {
	
	/**
	 * Creates a new IFileSorterTask object.
	 *
	 * @param file the file
	 * @return the i file task< void>
	 */
	public IFileTask<Void> createSorter(@Assisted final File file);
}
