package com.assignment.content_sorting.mocks;

import com.assignment.content_sorting.service.IService;

public class MockService implements IService {

	private boolean isStarted;

	@Override
	public void start() {
		this.isStarted = true;
	}

	public boolean isStarted() {
		return isStarted;
	}

	@Override
	public void addDependencies(IService dependentService) {

	}

}
