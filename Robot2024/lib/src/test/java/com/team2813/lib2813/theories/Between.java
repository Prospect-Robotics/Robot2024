package com.team2813.lib2813.theories;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.experimental.theories.ParametersSuppliedBy;

/**
 * Have an int parameter for a theory run with inputs between {@code first} and {@code last} inclusive.
 * If this annotation is repeated, all unique int values between all first and last inputs will be used,
 * with no gaurantee of order.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ParametersSuppliedBy(BetweenSupplier.class)
@Repeatable(BetweenRepeated.class)
public @interface Between {
	/**
	 * The first number to test
	 * 
	 * @return the first number to test
	 */
	int first();

	/**
	 * The last nubmer to test
	 * 
	 * @return the last number to test
	 */
	int last();
}
