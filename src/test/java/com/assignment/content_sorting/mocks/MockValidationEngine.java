package com.assignment.content_sorting.mocks;

import com.assignment.content_sorting.validation.engine.IValidationEngine;

public class MockValidationEngine<T> implements IValidationEngine<T>{

	private boolean validationFailure;

	@Override
	public boolean applyValidationRules(T data) {
		return !validationFailure;
	}
	
	public void setValidationFailure() {
		this.validationFailure = true;
	}

}
