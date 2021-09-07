package com.assignment.content_sorting.guice;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.inject.Named;

import com.assignment.content_sorting.exceptions.ContentSortingException;
import com.assignment.content_sorting.properties.IPropertiesLoader;
import com.assignment.content_sorting.properties.IServerConfig;
import com.assignment.content_sorting.properties.PropertiesLoader;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * The Class ContentSortingApplicationModule.
 * 
 * @author Rohan
 */
public class ContentSortingApplicationModule extends AbstractApplicationModule {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configure() {
		super.configure();
	}

	/**
	 * Gets the config.
	 *
	 * @return the config
	 * @throws ContentSortingException the content sorting exception
	 */
	@Provides
	@Singleton
	public IServerConfig getConfig() throws ContentSortingException {
		IPropertiesLoader propertiesLoader = new PropertiesLoader();
		return propertiesLoader.loadConfig();
	}

	/**
	 * Gets the executor service.
	 *
	 * @param serverConfig the server config
	 * @return the executor service
	 */
	@Provides
	@Singleton
	@Named("ContentSortingExecutor")
	public ExecutorService getExecutorService(final IServerConfig serverConfig) {
		return Executors.newFixedThreadPool(serverConfig.getConcurrencyLevel());
	}

	@Provides
	@Singleton
	@Named("AppLogger")
	public Logger getLogger() throws SecurityException, IOException {
		Logger logger = Logger.getLogger("ContentSorting");
		FileHandler handler = new FileHandler("./Application.log");
		handler.setFormatter(new SimpleFormatter());
		logger.addHandler(handler);
		return logger;
	}

}
