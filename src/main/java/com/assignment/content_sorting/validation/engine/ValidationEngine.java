package com.assignment.content_sorting.validation.engine;

import java.util.Set;

import com.assignment.content_sorting.validation.IValidationRule;
import com.google.inject.Inject;

public class ValidationEngine<T> implements IValidationEngine<T> {

	private final Set<IValidationRule<T>> validationRules;

	@Inject
	public ValidationEngine(final Set<IValidationRule<T>> validationRules) {
		this.validationRules = validationRules;
	}

	@Override
	public boolean applyValidationRules(T data) {
		return validationRules.stream().allMatch(rule -> rule.isValid(data));
	}

}
