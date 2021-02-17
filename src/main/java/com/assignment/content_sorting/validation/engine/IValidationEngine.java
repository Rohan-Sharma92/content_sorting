package com.assignment.content_sorting.validation.engine;

public interface IValidationEngine<T> {

	public boolean applyValidationRules(T data);
}
