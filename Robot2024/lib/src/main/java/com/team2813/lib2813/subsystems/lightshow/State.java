package com.team2813.lib2813.subsystems.lightshow;

import edu.wpi.first.wpilibj.util.Color;

public interface State {
	/**
	 * gets the color of this State.
	 * 
	 * @return the color of this State
	 */
	public Color color();

	/**
	 * Checks if the current state should be applied
	 * 
	 * @return {@code true} if the state should be applied
	 */
	public boolean apply();
}
