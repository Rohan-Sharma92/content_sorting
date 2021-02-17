package com.assignment.content_sorting.exceptions;

/**
 * The Class ContentSortingException.
 * @author Rohan
 */
public class ContentSortingException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3731437672744195438L;

	/**
	 * Instantiates a new content sorting exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public ContentSortingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new content sorting exception.
	 *
	 * @param message the message
	 */
	public ContentSortingException(String message) {
		super(message);
	}
}
