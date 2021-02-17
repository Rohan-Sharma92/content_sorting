package com.assignment.content_sorting.validation;

public interface IValidationRule<T> {

	public boolean isValid(T data);
}
