package com.assignment.content_sorting.mocks;

import com.assignment.content_sorting.validation.IValidationRule;

public class MockValidationRule<T> implements IValidationRule<T> {

	private boolean res = true;

	@Override
	public boolean isValid(T data) {
		return res;
	}

	public void markInvalid() {
		this.res = false;
	}

}
