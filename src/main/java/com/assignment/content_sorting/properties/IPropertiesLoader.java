
package com.assignment.content_sorting.properties;

import com.assignment.content_sorting.exceptions.ContentSortingException;

/**
 * The Interface IPropertiesLoader.
 * @author Rohan
 */
public interface IPropertiesLoader {

	/**
	 * Load config.
	 *
	 * @return the i server config
	 * @throws ContentSortingException the content sorting exception
	 */
	public IServerConfig loadConfig() throws ContentSortingException;

	/**
	 * Load logging info.
	 * @throws ContentSortingException 
	 */
	void loadLoggingInfo() throws ContentSortingException;
}
