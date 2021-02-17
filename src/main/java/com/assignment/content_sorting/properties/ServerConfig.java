package com.assignment.content_sorting.properties;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class ServerConfig.
 * @author Rohan
 */
public class ServerConfig implements IServerConfig {

	/** The Constant PROPERTY_MAX_CONCURRENCY_LEVEL. */
	public static final String PROPERTY_MAX_CONCURRENCY_LEVEL = "max_concurrency_level";
	
	/** The Constant PROPERTY_MAX_BATCH_SIZE. */
	public static final String PROPERTY_MAX_BATCH_SIZE = "max_batch_size";
	
	/** The Constant MAX_BATCH_SIZE. */
	public static final String MAX_BATCH_SIZE = "10485760";
	
	/** The Constant MAX_CONCURRENCY_LEVEL. */
	public static final String MAX_CONCURRENCY_LEVEL = "5";
	
	/** The Constant OUTPUT. */
	public static final String OUTPUT = "output";
	
	/** The Constant TEMP. */
	public static final String TEMP = "temp";
	
	/** The Constant LISTEN. */
	public static final String LISTEN = "listen";
	
	/** The listen dir. */
	private final String listenDir;
	
	/** The temp dir. */
	private final String tempDir;
	
	/** The output dir. */
	private final String outputDir;
	
	/** The concurrency level. */
	private final int concurrencyLevel;
	
	/** The max block size. */
	private final long maxBlockSize;

	/**
	 * Instantiates a new server config.
	 *
	 * @param properties the properties
	 */
	public ServerConfig(final Map<String, String> properties) {
		this.listenDir = properties.getOrDefault(LISTEN, LISTEN);
		this.tempDir = properties.getOrDefault(TEMP, TEMP);
		this.outputDir = properties.getOrDefault(OUTPUT, OUTPUT);
		this.concurrencyLevel = Integer.parseInt(properties.getOrDefault(PROPERTY_MAX_CONCURRENCY_LEVEL, MAX_CONCURRENCY_LEVEL));
		this.maxBlockSize = Long.parseLong(properties.getOrDefault(PROPERTY_MAX_BATCH_SIZE, MAX_BATCH_SIZE));
	}

	/**
	 * Instantiates a new server config.
	 */
	public ServerConfig() {
		this(new HashMap<String,String>());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getListenDirectory() {
		return this.listenDir;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTempDirectory() {
		return this.tempDir;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getOutputDirectory() {
		return this.outputDir;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getConcurrencyLevel() {
		return this.concurrencyLevel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMaxBatchSize() {
		return this.maxBlockSize;
	}

}
