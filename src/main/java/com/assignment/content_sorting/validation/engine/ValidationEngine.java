package com.assignment.content_sorting.validation.engine;

import java.util.Set;

import com.assignment.content_sorting.validation.IValidationRule;
import com.google.inject.Inject;

/**
 * The Class ValidationEngine.
 *
 * @author Rohan
 * @param <T> the generic type
 */
public class ValidationEngine<T> implements IValidationEngine<T> {

	/** The validation rules. */
	private final Set<IValidationRule<T>> validationRules;

	/**
	 * Instantiates a new validation engine.
	 *
	 * @param validationRules the validation rules
	 */
	@Inject
	public ValidationEngine(final Set<IValidationRule<T>> validationRules) {
		this.validationRules = validationRules;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean applyValidationRules(T data) {
		return validationRules.stream().allMatch(rule -> rule.isValid(data));
	}

}
