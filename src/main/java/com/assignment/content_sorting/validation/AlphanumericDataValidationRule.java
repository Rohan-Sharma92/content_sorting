package com.assignment.content_sorting.validation;

/**
 * The Class AlphanumericDataValidationRule.
 * @author Rohan
 */
public class AlphanumericDataValidationRule implements IValidationRule<String> {

	/** The Constant ALPHA_NUMERIC_REGEX. */
	private static final String ALPHA_NUMERIC_REGEX = "[A-Za-z0-9]+";

	/* (non-Javadoc)
	 * @see com.assignment.content_sorting.validation.IValidationRule#isValid(java.lang.Object)
	 */
	@Override
	public boolean isValid(String data) {
		return data.matches(ALPHA_NUMERIC_REGEX);
	}

}
