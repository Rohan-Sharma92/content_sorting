package com.assignment.content_sorting.util;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class SortedListTest {

	public void testSortedContent() throws FileNotFoundException {
		SortedList<String> sortedList = new SortedList<>();
		sortedList.add("abgf");
		sortedList.add("aaaa");
		Assert.assertEquals(sortedList.poll(),"aaaa");
		Assert.assertEquals(sortedList.poll(),"abgf");
	}
}
