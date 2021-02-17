package com.assignment.content_sorting.service;

/**
 * The Interface IService.
 * @author Rohan
 */
public interface IService {

	/**
	 * Start.
	 */
	public void start();
	
	/**
	 * Adds the dependencies.
	 *
	 * @param dependentService the dependent service
	 */
	public void addDependencies(IService dependentService);
	
}
