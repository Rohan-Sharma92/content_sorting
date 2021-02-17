package com.assignment.content_sorting.validation.engine;

import java.util.LinkedHashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.assignment.content_sorting.mocks.MockValidationRule;
import com.assignment.content_sorting.validation.IValidationRule;

@Test
public class ValidationRuleEngineTest {

	
	public void testValidationEngineFailsWhenOneRuleFails() {
		Set<IValidationRule<String>> validationRules= new LinkedHashSet<>();
		MockValidationRule<String> rule1 = new MockValidationRule<>();
		MockValidationRule<String> rule2 = new MockValidationRule<>();
		rule2.markInvalid();
		MockValidationRule<String> rule3 = new MockValidationRule<>();
		validationRules.add(rule1);
		validationRules.add(rule2);
		validationRules.add(rule3);
		ValidationEngine<String> engine = new ValidationEngine<>(validationRules);
		String data="test";
		Assert.assertFalse(engine.applyValidationRules(data));
	}
	public void testValidationEnginePasses() {
		Set<IValidationRule<String>> validationRules= new LinkedHashSet<>();
		MockValidationRule<String> rule1 = new MockValidationRule<>();
		MockValidationRule<String> rule2 = new MockValidationRule<>();
		MockValidationRule<String> rule3 = new MockValidationRule<>();
		validationRules.add(rule1);
		validationRules.add(rule2);
		validationRules.add(rule3);
		ValidationEngine<String> engine = new ValidationEngine<>(validationRules);
		String data="test";
		Assert.assertTrue(engine.applyValidationRules(data));
	}
}
