package com.assignment.content_sorting.properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.assignment.content_sorting.exceptions.ContentSortingException;

/**
 * The Class PropertiesLoader.
 * @author Rohan
 */
public class PropertiesLoader implements IPropertiesLoader {

	private static final String CONFIG_SERVER_PROPERTIES = "config/server.properties";
	/** The Constant SEPARATOR. */
	private static final String SEPARATOR = "=";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IServerConfig loadConfig() throws ContentSortingException {
		IServerConfig config = null;
		try {
			BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(CONFIG_SERVER_PROPERTIES));
			Map<String, String> propertyMap = new HashMap<>();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				String[] props = line.split(SEPARATOR);
				propertyMap.put(props[0], props[1]);
			}
			config = new ServerConfig(propertyMap);
		} catch (IOException e) {
			return new ServerConfig();
		} catch (Exception e) {
			throw new ContentSortingException("Incorrect properties format ", e);
		}
		return config;
	}

}
