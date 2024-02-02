package com.team2813.lib2813.subsystems;

import java.util.Objects;

import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.Encoder;
import com.team2813.lib2813.control.Motor;
import com.team2813.lib2813.control.PIDMotor;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;

/**
 * Defines PID controll over a motor, with values specified by an encoder
 * 
 * @param <T> the {@link MotorSubsystem.Position} type to use positions from.
 */
public abstract class MotorSubsystem<T extends MotorSubsystem.Position> extends PIDSubsystem {

	protected final Motor motor;
	protected final Encoder encoder;

	protected MotorSubsystem(MotorSubsystemConfiguration builder) {
		super(builder.controller, builder.startingPosition);
		getController().setTolerance(builder.acceptableError);
		motor = builder.motor;
		encoder = builder.encoder;
	}

	/**
	 * A configuration for a MotorSubsystem
	 * 
	 */
	public static class MotorSubsystemConfiguration {
		/**
		 * The default error of a motor subsystems
		 */
		public static final double DEFAULT_ERROR = 5.0;
		/**
		 * The default starting position if one is not defined
		 */
		public static final double DEFAULT_STARTING_POSITION = 0.0;
		protected Motor motor;
		protected Encoder encoder;
		private PIDController controller;
		private double acceptableError;
		private double startingPosition;

		/**
		 * Creates a new config for MotorSubsystems.
		 * The default acceptable error is {@value #DEFAULT_ERROR},
		 * the PID constants are set to 0,
		 * and the starting position is {@value #DEFAULT_STARTING_POSITION}
		 * 
		 * @param motor   the motor to use
		 * @param encoder the encoder to use
		 */
		public MotorSubsystemConfiguration(Motor motor, Encoder encoder) {
			this.motor = Objects.requireNonNull(motor, "motor should not be null");
			this.encoder = Objects.requireNonNull(encoder, "encoder should not be null");
			controller = new PIDController(0, 0, 0);
			acceptableError = DEFAULT_ERROR;
			startingPosition = DEFAULT_STARTING_POSITION;
		}

		/**
		 * Creates a new config for MotorSubsystems using a motor that has a built-in
		 * encoder.
		 * The default acceptable error is {@value #DEFAULT_ERROR},
		 * the PID constants are set to 0,
		 * and the starting position is {@value #DEFAULT_STARTING_POSITION}
		 * 
		 * @param motor the motor to use that also supports an encoder
		 */
		public MotorSubsystemConfiguration(PIDMotor motor) {
			this(motor, motor);
		}

		/**
		 * Sets the controller used to calculate the next value
		 * 
		 * @param controller
		 * @return {@code this} for chaining
		 */
		public MotorSubsystemConfiguration controller(PIDController controller) {
			this.controller = controller;
			return this;
		}

		/**
		 * Sets the PID constants for the controller
		 * 
		 * @param p the preportianal
		 * @param i the integral
		 * @param d the derivative
		 * @return {@code this} for chaining
		 */
		public MotorSubsystemConfiguration PID(int p, int i, int d) {
			controller.setPID(p, i, d);
			return this;
		}

		/**
		 * sets the starting position
		 * 
		 * @param startingPosition the position to start at
		 * @return {@code this} for chaining
		 */
		public MotorSubsystemConfiguration startingPosition(double startingPosition) {
			this.startingPosition = startingPosition;
			return this;
		}

		public MotorSubsystemConfiguration startingPosition(Position startingPosition) {
			this.startingPosition = startingPosition.getPos();
			return this;
		}

		public MotorSubsystemConfiguration acceptableError(double acceptableError) {
			this.acceptableError = acceptableError;
			return this;
		}
	}

	/**
	 * A position that the MotorSubsystem can go to
	 */
	public interface Position {
		/**
		 * gets the position of this enum value
		 * @return the position as a double
		 */
		double getPos();
	}

	/**
	 * Sets the desired setpoint to the current setpoint, and enables the PID.
	 * control.
	 *
	 * <p>Prefer calling this over calling {@link #setSetpoint(double)}.
	 * 
	 * @param setpoint the position to go to
	 */
	public void setSetpoint(T setpoint) {
		if (!isEnabled()) {
			enable();
		}
		setSetpoint(setpoint.getPos());
	}

	public void set(ControlMode mode, double demand, double feedForward) {
		if (isEnabled()) {
			disable();
		}
		motor.set(mode, demand, feedForward);
	}

	public void set(ControlMode mode, double demand) {
		if (isEnabled()) {
			disable();
		}
		motor.set(mode, demand);
	}

	@Override
	protected void useOutput(double output, double setpoint) {
		motor.set(ControlMode.DUTY_CYCLE, output);
	}

	@Override
	protected double getMeasurement() {
		return encoder.position();
	}
}
