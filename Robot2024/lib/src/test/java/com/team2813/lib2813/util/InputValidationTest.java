package com.team2813.lib2813.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.team2813.lib2813.theories.Between;

@RunWith(Theories.class)
public class InputValidationTest {
	@Theory
	public void invalidCanID(
			@Between(first = -50, last = -1) @Between(first = 63, last = 100) int canID) {
		InvalidCanIdException expected = new InvalidCanIdException(canID);
		InvalidCanIdException actual = assertThrows(
				InvalidCanIdException.class,
				() -> InputValidation.checkCanId(canID));
		assertEquals("Messages were not identical", expected, actual);
	}

	private static <T> RuntimeException outOfBounds(T val) {
		return new RuntimeException("Value was out of bounds");
	}

	@Theory
	public void genericInvalidTest(@Between(first = 0, last = 4) int unused) {
		Integer min = Integer.valueOf(0);
		Integer actual = Integer.valueOf(unused);
		Integer max = Integer.valueOf(4);
		InputValidation.checkBounds(min, max, actual, InputValidationTest::outOfBounds);
	}

	@Theory
	public void validCanID(@Between(first = 0, last = 62) int canID) {
		assertEquals(canID, InputValidation.checkCanId(canID));
	}
}
