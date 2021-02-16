package com.assignment.content_sorting.mocks;

import java.io.File;
import java.util.Set;

import com.assignment.content_sorting.file.common.IFileTask;
import com.assignment.content_sorting.file.factories.IFileMergeTaskFactory;

public class MockMergeTaskFactory implements IFileMergeTaskFactory {

	private MockFileTask<Void> task;
	
	@Override
	public IFileTask<Void> createMergeTask(String fileName, Set<File> fragments) {
		return task;
	}
	
	public void setExpectedTask(MockFileTask<Void> task) {
		this.task = task;
	}

}
