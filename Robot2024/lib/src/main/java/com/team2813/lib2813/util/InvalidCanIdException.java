package com.team2813.lib2813.util;

import java.lang.RuntimeException;

/**
 * Signifies that a {@index CAN} Id was given to a function, and it was invalid
 */
public class InvalidCanIdException extends RuntimeException {
	/**
	 * The CAN id that is invalid
	 * @serial an integer that is not between 0 and 62
	 */
	private int canId;
	public InvalidCanIdException(int canId) {
		super(String.format(
			"%d is not a valid can id (a valid can id is between 0 and 62, inclusive)",
			canId));
		if (0 <= canId && canId <= 62) {
			throw new IllegalArgumentException(String.format(
				"%s is a valid can id (it is between 0 and 62, inclusive)", canId));
		}
	}
	public int getCanId() {
		return canId;
	}
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof InvalidCanIdException))
			return false;
		InvalidCanIdException other = (InvalidCanIdException) o;
		return getMessage().equals(other.getMessage());
	}
	@Override
	public int hashCode() {
		return getMessage().hashCode();
	}
}
