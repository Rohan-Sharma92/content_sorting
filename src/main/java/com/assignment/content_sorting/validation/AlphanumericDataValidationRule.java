package com.assignment.content_sorting.validation;

public class AlphanumericDataValidationRule implements IValidationRule<String> {

	private static final String ALPHA_NUMERIC_REGEX = "[A-Za-z0-9]+";

	@Override
	public boolean isValid(String data) {
		return data.matches(ALPHA_NUMERIC_REGEX);
	}

}
