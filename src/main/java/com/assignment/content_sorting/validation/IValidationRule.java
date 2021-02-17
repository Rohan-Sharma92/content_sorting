package com.assignment.content_sorting.validation;

/**
 * The Interface IValidationRule.
 * 
 * @author Rohan
 * @param <T> the generic type
 */
public interface IValidationRule<T> {

	/**
	 * Checks if is valid.
	 *
	 * @param data the data
	 * @return true, if is valid
	 */
	public boolean isValid(T data);
}
