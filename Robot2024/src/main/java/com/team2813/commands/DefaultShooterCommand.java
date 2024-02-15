package com.team2813.commands;

import java.util.function.DoubleSupplier;

import com.team2813.lib2813.control.ControlMode;
import com.team2813.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * The default command for the {@link Shooter} subsystem.
 */
public final class DefaultShooterCommand extends Command {
	private final Shooter shooter;
	private final DoubleSupplier control;

	/**
	 * Constructor.
	 *
	 * @param shooter The shooter subsystem.
	 * @param control Provides values from the driver controller;
	 *                the returned values MUST be less than or equal to 1.
	 */
	public DefaultShooterCommand(Shooter shooter, DoubleSupplier control) {
		this.shooter = shooter;
		this.control = control;
		addRequirements(shooter);
	}

	/**
	 * The number to multiply results from {@link #control}. Multiplies by {@value #MULTIPLIER}
	 */
	private static final double MULTIPLIER = 0.5;

	/**
	 * The maximum value that the controler can have when no one is touching it.
	 *
	 * <p>The controler does not always snap back to the center when it is released, so this value accounts for that.
	 */
	private static final double DEADZONE = 0.01;

	/**
	 * Sets the motor to the value from the {@link DoubleSupplier} {@link #control}.
	 * Multiplies the result by {@value #MULTIPLIER}. The return from {@link #control} MUST be less than or equal to 1.
	 */
	@Override
	public void execute() {
		double val = control.getAsDouble();
		if (Math.abs(val) > DEADZONE) {
			shooter.set(ControlMode.DUTY_CYCLE, val * MULTIPLIER);
		} else {
			shooter.set(ControlMode.DUTY_CYCLE, 0);
		}
	}
}
