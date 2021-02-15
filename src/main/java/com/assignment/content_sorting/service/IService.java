package com.assignment.content_sorting.service;

public interface IService {

	public void start();
	
	public void addDependencies(IService dependentService);
	
}
