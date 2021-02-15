package com.assignment.content_sorting.file.factories;

import com.assignment.content_sorting.file.splitter.IFileProcessEnqueuer;

public interface IFileSplittingEnqueuerFactory {

	IFileProcessEnqueuer createFileSplittingExecutionRequest();
}
