package com.team2813.lib2813.control;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.revrobotics.CANSparkBase.ControlType;

public enum ControlMode {
	DUTY_CYCLE(TalonFXControlMode.PercentOutput, ControlType.kDutyCycle),
	VELOCITY(TalonFXControlMode.Velocity, ControlType.kVelocity),
	MOTION_MAGIC(TalonFXControlMode.MotionMagic, ControlType.kPosition);

	private final TalonFXControlMode talonMode;
	private final ControlType sparkMode;

	ControlMode(TalonFXControlMode talonMode, ControlType sparkMode) {
		this.talonMode = talonMode;
		this.sparkMode = sparkMode;
	}

	public TalonFXControlMode getTalonMode() {
		return talonMode;
	}
	public ControlType getSparkMode() {
		return sparkMode;
	}
}
