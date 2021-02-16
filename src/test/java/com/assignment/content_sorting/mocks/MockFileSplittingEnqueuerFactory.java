package com.assignment.content_sorting.mocks;

import java.util.Iterator;
import java.util.List;

import com.assignment.content_sorting.file.factories.IFileSplittingEnqueuerFactory;
import com.assignment.content_sorting.file.splitter.IFileProcessEnqueuer;

public class MockFileSplittingEnqueuerFactory implements IFileSplittingEnqueuerFactory {

	private Iterator<IFileProcessEnqueuer> iterator;

	@Override
	public IFileProcessEnqueuer createFileSplittingExecutionRequest() {
		return iterator.next();
	}
	
	public void setExpectedEnqueuer(List<IFileProcessEnqueuer> enqueuers) {
		this.iterator = enqueuers.iterator();
	}

}
