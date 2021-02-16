package com.assignment.content_sorting.properties;

import com.assignment.content_sorting.exceptions.ContentSortingException;

public interface IPropertiesLoader {

	public IServerConfig loadConfig() throws ContentSortingException;
}
