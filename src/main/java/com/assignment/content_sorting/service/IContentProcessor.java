package com.assignment.content_sorting.service;

import java.util.concurrent.CompletableFuture;

public interface IContentProcessor {
	public CompletableFuture<Void> process();
}
