package com.team2813.lib2813.util;

/**
 * Signifies that a {@index CAN} Id was given to a function, and it was invalid
 */
public class InvalidCanIdException extends IllegalArgumentException {
	/**
	 * The CAN id that is invalid
	 * @serial an integer that is not between 0 and 62
	 */
	private final int canId;

	public InvalidCanIdException(int canId, String message) {
		super(message);
		this.canId = canId;
	}

	public InvalidCanIdException(int canId) {
		this(canId, String.format(
			"%d is not a valid can ID (a valid can id is between 0 and 62, inclusive)",
			canId));
		if (0 <= canId && canId <= 62) {
			throw new IllegalArgumentException(String.format(
				"%s is a valid can ID (it is between 0 and 62, inclusive)", canId));
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
