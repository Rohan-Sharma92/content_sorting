package com.assignment.content_sorting.properties;

import java.util.HashMap;
import java.util.Map;

public class ServerConfig implements IServerConfig {

	public static final String PROPERTY_MAX_CONCURRENCY_LEVEL = "max_concurrency_level";
	public static final String PROPERTY_MAX_BATCH_SIZE = "max_batch_size";
	public static final String MAX_BATCH_SIZE = "10485760";
	public static final String MAX_CONCURRENCY_LEVEL = "5";
	public static final String OUTPUT = "output";
	public static final String TEMP = "temp";
	public static final String LISTEN = "listen";
	private final String listenDir;
	private final String tempDir;
	private final String outputDir;
	private final int concurrencyLevel;
	private final long maxBlockSize;

	public ServerConfig(final Map<String, String> properties) {
		this.listenDir = properties.getOrDefault(LISTEN, LISTEN);
		this.tempDir = properties.getOrDefault(TEMP, TEMP);
		this.outputDir = properties.getOrDefault(OUTPUT, OUTPUT);
		this.concurrencyLevel = Integer.parseInt(properties.getOrDefault(PROPERTY_MAX_CONCURRENCY_LEVEL, MAX_CONCURRENCY_LEVEL));
		this.maxBlockSize = Long.parseLong(properties.getOrDefault(PROPERTY_MAX_BATCH_SIZE, MAX_BATCH_SIZE));
	}

	public ServerConfig() {
		this(new HashMap<String,String>());
	}

	@Override
	public String getListenDirectory() {
		return this.listenDir;
	}

	@Override
	public String getTempDirectory() {
		return this.tempDir;
	}

	@Override
	public String getOutputDirectory() {
		return this.outputDir;
	}

	@Override
	public int getConcurrencyLevel() {
		return this.concurrencyLevel;
	}

	@Override
	public long getMaxBatchSize() {
		return this.maxBlockSize;
	}

}
