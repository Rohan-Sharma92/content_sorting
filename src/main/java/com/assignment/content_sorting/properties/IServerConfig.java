package com.assignment.content_sorting.properties;

/**
 * The Interface IProperties.
 * 
 * @author Rohan
 */
public interface IServerConfig {

	/**
	 * Gets the listen directory.
	 *
	 * @return the listen directory
	 */
	public String getListenDirectory();

	/**
	 * Gets the temp directory.
	 *
	 * @return the temp directory
	 */
	public String getTempDirectory();

	/**
	 * Gets the output directory.
	 *
	 * @return the output directory
	 */
	public String getOutputDirectory();

	/**
	 * Gets the concurrency level.
	 *
	 * @return the concurrency level
	 */
	public int getConcurrencyLevel();

	/**
	 * Gets the max batch size.
	 *
	 * @return the max batch size
	 */
	public long getMaxBatchSize();
}
