package com.team2813.lib2813.control;

public interface Motor {
	// motor control
	void set(ControlMode mode, double demand);
	void set(ControlMode mode, double demand, double feedForward);
}
