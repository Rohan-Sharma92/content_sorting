package com.assignment.content_sorting.file.merge;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

public interface IFileMerger {

	CompletableFuture<Void> merge(LinkedList<String> orderedFileNames) throws IOException;

}
