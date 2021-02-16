package com.assignment.content_sorting.mocks;

import java.util.List;

import com.assignment.content_sorting.file.common.IFileTask;
import com.assignment.content_sorting.file.factories.IFileSplitterTaskFactory;
import com.assignment.content_sorting.file.reader.IFileWrapper;

public class MockFileSplitTaskFactory implements IFileSplitterTaskFactory{

	private List<MockFileTask<Void>> list;

	@Override
	public IFileTask<Void> createSplitter(IFileWrapper fileWrapper) {
		MockFileTask<Void> task = new MockFileTask<>();
		list.add(task);
		return task;
	}
	
	public List<MockFileTask<Void>> getTriggeredTasks() {
		return list;
	}

}
