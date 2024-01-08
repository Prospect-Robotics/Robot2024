package com.team2813.lib2813.util;

import java.util.function.Function;
import java.util.function.IntFunction;

public class InputValidation {
	private InputValidation() {
		throw new AssertionError("non instiantiable");
	}

	/**
	 * Check if a given value is between the bounds
	 * @param <T> the type to compare
	 * @param lower an object that {@code actual} should be comparativly less than or equal to
	 * @param upper an object that {@code actual} should be comparativly greater than or equal to
	 * @param actual the actual value
	 * @param throwable a function that takes the actual value and returns an unchecked exception
	 * @throws RuntimeException when actual is not between lower and upper, based on natural ordering.
	 * The exception thrown is provided by {@code throwable}
	 */
	static <T extends Comparable<T>> void checkBounds(T lower, T upper, T actual, Function<? super T, RuntimeException> throwable) {
		assert lower.compareTo(upper) <= 0;
		if (!(lower.compareTo(actual) <= 0 && actual.compareTo(upper) <= 0)) {
			throw throwable.apply(actual);
		}
	}
	/**
	 * Check if the given value is in the bounds
	 * @param lower the lower bound
	 * @param upper the upper bound
	 * @param actual the actual value
	 * @param throwable a function that takes the actual value and returns an unchecked exception
	 * @throws RuntimeException when the actual is not inbetween the bounds.
	 * exception is provided by {@code throwable}
	 */
	static void checkBounds(int lower, int upper, int actual, IntFunction<RuntimeException> throwable) {
		assert lower <= upper;
		if (!(lower <= actual && actual <= upper)) {
			throw throwable.apply(actual);
		}
	}
	/**
	 * Checks if the input is a valid {@index CAN} Id, and throws an exception if it isn't
	 * @param id the can id, between 0 and 62, inclusive
	 * @return the {@code id}
	 * @throws InvalidCanIdException if the id is invalid
	 */
	public static int checkCanId(int id) {
		checkBounds(0, 62, id, InvalidCanIdException::new);
		return id;
	}
}
