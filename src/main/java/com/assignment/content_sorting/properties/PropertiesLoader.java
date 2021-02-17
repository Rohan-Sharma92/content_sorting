package com.assignment.content_sorting.properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.assignment.content_sorting.exceptions.ContentSortingException;

public class PropertiesLoader implements IPropertiesLoader {

	@Override
	public IServerConfig loadConfig() throws ContentSortingException {
		IServerConfig config = null;
		try {
			BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("config/server.properties"));
			Map<String, String> propertyMap = new HashMap<>();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				String[] props = line.split("=");
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
