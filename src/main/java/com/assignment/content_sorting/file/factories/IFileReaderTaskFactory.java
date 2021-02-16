package com.assignment.content_sorting.file.factories;

import com.assignment.content_sorting.file.common.IFileTask;
import com.assignment.content_sorting.file.reader.IFileWrapper;
import com.google.inject.assistedinject.Assisted;

public interface IFileReaderTaskFactory {

	public IFileTask<Boolean> createReader(@Assisted final IFileWrapper fileWrapper);
}
