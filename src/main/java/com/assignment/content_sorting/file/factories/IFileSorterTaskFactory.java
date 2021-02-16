package com.assignment.content_sorting.file.factories;

import java.io.File;

import com.assignment.content_sorting.file.common.IFileTask;
import com.google.inject.assistedinject.Assisted;

public interface IFileSorterTaskFactory {
	public IFileTask<Void> createSorter(@Assisted final File file);
}
