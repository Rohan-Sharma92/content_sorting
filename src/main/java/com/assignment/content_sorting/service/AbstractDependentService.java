package com.assignment.content_sorting.service;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class AbstractDependentService.
 * @author Rohan
 */
public abstract class AbstractDependentService implements IService {

	/** The dependent services. */
	private final List<IService> dependentServices = new ArrayList<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDependencies(IService dependentService) {
		this.dependentServices.add(dependentService);
	}
	
	/**
	 * Notify availability.
	 */
	protected void notifyAvailability() {
		this.dependentServices.stream().forEach(service -> service.start());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() {
		performProcessing();
		notifyAvailability();
	}

	/**
	 * Perform processing.
	 */
	protected abstract void performProcessing();

}
