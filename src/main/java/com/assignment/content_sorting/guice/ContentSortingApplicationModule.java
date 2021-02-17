package com.assignment.content_sorting.guice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Named;

import com.assignment.content_sorting.exceptions.ContentSortingException;
import com.assignment.content_sorting.file.cache.ITempFileCache;
import com.assignment.content_sorting.file.cache.TempFileCache;
import com.assignment.content_sorting.file.common.IFileTask;
import com.assignment.content_sorting.file.factories.IFileMergeTaskFactory;
import com.assignment.content_sorting.file.factories.IFileReaderTaskFactory;
import com.assignment.content_sorting.file.factories.IFileSorterTaskFactory;
import com.assignment.content_sorting.file.factories.IFileSplitterTaskFactory;
import com.assignment.content_sorting.file.factories.IFileSplittingEnqueuerFactory;
import com.assignment.content_sorting.file.merge.FileMergeTask;
import com.assignment.content_sorting.file.merge.ProcessedFileMerger;
import com.assignment.content_sorting.file.merge.TempFileMerger;
import com.assignment.content_sorting.file.reader.FileReaderTask;
import com.assignment.content_sorting.file.sort.FileSorter;
import com.assignment.content_sorting.file.sort.FileSorterTask;
import com.assignment.content_sorting.file.splitter.FileSplitter;
import com.assignment.content_sorting.file.splitter.FileSplitterTask;
import com.assignment.content_sorting.file.splitter.FileSplittingEnqueuer;
import com.assignment.content_sorting.file.splitter.IFileProcessEnqueuer;
import com.assignment.content_sorting.properties.IPropertiesLoader;
import com.assignment.content_sorting.properties.IServerConfig;
import com.assignment.content_sorting.properties.PropertiesLoader;
import com.assignment.content_sorting.service.ContentSortingService;
import com.assignment.content_sorting.service.IContentProcessor;
import com.assignment.content_sorting.service.IService;
import com.assignment.content_sorting.service.InitialisationService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

public class ContentSortingApplicationModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(IFileProcessEnqueuer.class, FileSplittingEnqueuer.class)
				.build(IFileSplittingEnqueuerFactory.class));
		install(new FactoryModuleBuilder().implement(new TypeLiteral<IFileTask<Void>>() {
		}, FileReaderTask.class).build(IFileReaderTaskFactory.class));
		install(new FactoryModuleBuilder().implement(new TypeLiteral<IFileTask<Void>>() {
		}, FileSplitterTask.class).build(IFileSplitterTaskFactory.class));
		install(new FactoryModuleBuilder().implement(new TypeLiteral<IFileTask<Void>>() {
		}, FileSorterTask.class).build(IFileSorterTaskFactory.class));
		install(new FactoryModuleBuilder().implement(new TypeLiteral<IFileTask<Void>>() {
		}, FileMergeTask.class).build(IFileMergeTaskFactory.class));
		bind(ITempFileCache.class).to(TempFileCache.class).in(Scopes.SINGLETON);
		bind(IContentProcessor.class).annotatedWith(Names.named("FileSplitter")).to(FileSplitter.class)
				.in(Scopes.SINGLETON);
		bind(IContentProcessor.class).annotatedWith(Names.named("FileSorter")).to(FileSorter.class)
				.in(Scopes.SINGLETON);
		bind(IContentProcessor.class).annotatedWith(Names.named("TempFileMerge")).to(TempFileMerger.class)
				.in(Scopes.SINGLETON);
		bind(IContentProcessor.class).annotatedWith(Names.named("ProcessedFileMerge")).to(ProcessedFileMerger.class)
				.in(Scopes.SINGLETON);
		bind(IService.class).annotatedWith(Names.named("InitialisationService")).to(InitialisationService.class)
				.in(Scopes.SINGLETON);
		bind(IService.class).annotatedWith(Names.named("ContentService")).to(ContentSortingService.class)
				.in(Scopes.SINGLETON);
	}

	@Provides
	@Singleton
	public IServerConfig getConfig() throws ContentSortingException {
		IPropertiesLoader propertiesLoader = new PropertiesLoader();
		return propertiesLoader.loadConfig();
	}

	@Provides
	@Singleton
	@Named("FileSplittingExecutor")
	public ExecutorService getExecutorService(final IServerConfig serverConfig) {
		return Executors.newFixedThreadPool(serverConfig.getConcurrencyLevel());
	}

}
