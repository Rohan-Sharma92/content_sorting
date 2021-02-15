package com.assignment.content_sorting.service;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDependentService implements IService {

	private final List<IService> dependentServices = new ArrayList<>();

	@Override
	public void addDependencies(IService dependentService) {
		this.dependentServices.add(dependentService);
	}
	
	protected void notifyAvailability() {
		this.dependentServices.stream().forEach(Service -> Service.start());
	}
	
	@Override
	public void start() {
		performProcessing();
		notifyAvailability();
	}

	protected abstract void performProcessing();

}
