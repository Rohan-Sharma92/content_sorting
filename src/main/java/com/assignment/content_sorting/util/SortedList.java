package com.assignment.content_sorting.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The Class SortedList.
 *
 * @param <T> the generic type
 */
public class SortedList<T extends Comparable<T>> extends LinkedList<T> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The buffer. */
	private List<T> buffer = new LinkedList<T>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(T e) {
		buffer.clear();
		buffer.addAll(this);
		buffer.add(e);
		Collections.sort(buffer);
		int finalIdx = buffer.indexOf(e);
		add(finalIdx, e);
		return true;
	}
}
