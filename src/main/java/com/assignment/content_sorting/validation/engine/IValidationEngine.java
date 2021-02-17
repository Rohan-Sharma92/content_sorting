package com.assignment.content_sorting.validation.engine;

/**
 * The Interface IValidationEngine.
 * @author Rohan
 *
 * @param <T> the generic type
 */
public interface IValidationEngine<T> {

	/**
	 * Apply validation rules.
	 *
	 * @param data the data
	 * @return true, if successful
	 */
	public boolean applyValidationRules(T data);
}
