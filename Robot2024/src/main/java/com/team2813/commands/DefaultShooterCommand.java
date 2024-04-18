package com.team2813.commands;

import java.util.function.DoubleSupplier;

import com.team2813.lib2813.control.ControlMode;
import com.team2813.subsystems.ShooterPivot;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * The default command for the {@link ShooterPivot} subsystem.
 */
public final class DefaultShooterCommand extends Command {
	private final ShooterPivot shooterPivot;
	private final DoubleSupplier control;

	/**
	 * Constructor.
	 *
	 * @param shooter The shooter subsystem.
	 * @param control Provides values from the driver controller;
	 *                the returned values MUST be less than or equal to 1.
	 */
	public DefaultShooterCommand(ShooterPivot shooterPivot, DoubleSupplier control) {
		this.shooterPivot = shooterPivot;
		this.control = control;
		addRequirements(shooterPivot);
	}

	/**
	 * The number to multiply results from {@link #control}. Multiplies by {@value #MULTIPLIER}
	 */
	private static final double MULTIPLIER = 0.3;

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
			shooterPivot.set(ControlMode.DUTY_CYCLE, val * MULTIPLIER);
		} else if (!shooterPivot.isEnabled()) {
			shooterPivot.set(ControlMode.DUTY_CYCLE, 0);
		}
	}
}
