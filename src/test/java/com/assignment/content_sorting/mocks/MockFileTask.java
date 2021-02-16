package com.assignment.content_sorting.mocks;

import com.assignment.content_sorting.file.common.IFileTask;

public class MockFileTask<T> implements IFileTask<T> {

	private boolean isTaskExecuted;

	@Override
	public T call() throws Exception {
		this.isTaskExecuted = true;
		return null;
	}

	public boolean isTaskExecuted() {
		return isTaskExecuted;
	}

}
