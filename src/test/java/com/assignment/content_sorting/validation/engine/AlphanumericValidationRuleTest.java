package com.assignment.content_sorting.validation.engine;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.assignment.content_sorting.validation.AlphanumericDataValidationRule;

@Test
public class AlphanumericValidationRuleTest {

	public void testRulePassesForAlphabeticalData() {
		AlphanumericDataValidationRule rule = new AlphanumericDataValidationRule();
		Assert.assertTrue(rule.isValid("test"));
	}
	
	public void testRulePassesForNumericalData() {
		AlphanumericDataValidationRule rule = new AlphanumericDataValidationRule();
		Assert.assertTrue(rule.isValid("1234"));
	}
	
	public void testRuleFailsForNonAlphanumericalData() {
		AlphanumericDataValidationRule rule = new AlphanumericDataValidationRule();
		Assert.assertFalse(rule.isValid("abc?*"));
	}
}
