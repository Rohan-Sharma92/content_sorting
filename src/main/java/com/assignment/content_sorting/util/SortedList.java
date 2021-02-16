package com.assignment.content_sorting.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SortedList<T extends Comparable<T>> extends LinkedList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<T> buffer = new LinkedList<T>();

	@Override
	public boolean add(T e) {
		buffer.clear();
		Iterator<T> iterator = iterator();
		while (iterator.hasNext())
			buffer.add(iterator.next());
		buffer.add(e);
		Collections.sort(buffer);
		int finalIdx = buffer.indexOf(e);
		add(finalIdx, e);
		return true;
	}
}
