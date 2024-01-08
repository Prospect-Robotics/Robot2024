package com.team2813.lib2813.theories;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.experimental.theories.ParametersSuppliedBy;

@Target(ElementType.PARAMETER)
@ParametersSuppliedBy(BetweenSupplier.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface BetweenRepeated {
	public Between[] value();
}
