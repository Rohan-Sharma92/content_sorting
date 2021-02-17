package com.assignment.content_sorting.file.factories;

import com.assignment.content_sorting.file.common.IFileTask;
import com.assignment.content_sorting.file.reader.IFileWrapper;
import com.google.inject.assistedinject.Assisted;

/**
 * A factory for creating IFileSplitterTask objects.
 */
public interface IFileSplitterTaskFactory {
	
	/**
	 * Creates a new IFileSplitterTask object.
	 *
	 * @param fileWrapper the file wrapper
	 * @return the i file task< void>
	 */
	public IFileTask<Void> createSplitter(@Assisted final IFileWrapper fileWrapper);
}
