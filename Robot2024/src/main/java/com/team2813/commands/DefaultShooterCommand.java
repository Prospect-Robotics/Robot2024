package com.team2813.commands;

import java.util.function.DoubleSupplier;

import com.team2813.lib2813.control.ControlMode;
import com.team2813.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.Command;

public class DefaultShooterCommand extends Command {
	private Shooter shooter;
	private DoubleSupplier controll;
	public DefaultShooterCommand(Shooter shooter, DoubleSupplier controll) {
		this.shooter = shooter;
		this.controll = controll;
		addRequirements(shooter);
	}
	/**
	 * The number to multiply results from {@link #controll}. Multiplies by {@value #MULTIPLIER}
	 */
	private static final double MULTIPLIER = 1;
	private static final double DEADZONE = 0.01;
	/**
	 * Sets the motor to the value from the {@link DoubleSupplier} {@link #controll}.
	 * Multiplies the result by {@value #MULTIPLIER}. The return from {@link #controll} MUST be less than or equal to 1.
	 */
	public void excecute() {
		double val = controll.getAsDouble();
		if (Math.abs(val) > DEADZONE) {
			shooter.set(ControlMode.DUTY_CYCLE, val * MULTIPLIER);
		} else {
			shooter.set(ControlMode.DUTY_CYCLE, 0);
		}
	}
}
