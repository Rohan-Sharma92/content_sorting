package com.assignment.content_sorting.file.factories;

import com.assignment.content_sorting.file.common.IFileTask;
import com.assignment.content_sorting.file.reader.IFileWrapper;
import com.google.inject.assistedinject.Assisted;

public interface IFileSplitterTaskFactory {
	public IFileTask<Void> createSplitter(@Assisted final IFileWrapper fileWrapper);
}
