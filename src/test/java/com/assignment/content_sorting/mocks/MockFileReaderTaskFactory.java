package com.assignment.content_sorting.mocks;

import java.util.Iterator;
import java.util.List;

import com.assignment.content_sorting.file.common.IFileTask;
import com.assignment.content_sorting.file.factories.IFileReaderTaskFactory;
import com.assignment.content_sorting.file.reader.IFileWrapper;

public class MockFileReaderTaskFactory implements IFileReaderTaskFactory {

	private Iterator<MockFileTask<Boolean>> iterator;

	public void setExpectedTask(List<MockFileTask<Boolean>> tasks) {
		iterator = tasks.iterator();
	}

	@Override
	public IFileTask<Boolean> createReader(IFileWrapper fileWrapper) {
		return iterator.next();
	}

}
