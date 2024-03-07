package com.team2813.subsystems;

import static com.team2813.Constants.SHOOTER_ENCODER;
import static com.team2813.Constants.SHOOTER_PIVOT;

import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.PIDMotor;
import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.subsystems.MotorSubsystem;
import com.team2813.lib2813.util.ConfigUtils;

public class ShooterPivot extends MotorSubsystem<ShooterPivot.Position> {
	private static final double ERROR = 0.05;
	public ShooterPivot() {
		super(new MotorSubsystemConfiguration(
				pivotMotor()).acceptableError(ERROR));
	}

	private static PIDMotor pivotMotor() {
		TalonFXWrapper result = new TalonFXWrapper(SHOOTER_PIVOT, InvertType.CLOCKWISE);
		result.setNeutralMode(NeutralModeValue.Brake);
		TalonFXConfigurator config = result.motor().getConfigurator();
		ConfigUtils.phoenix6Config(
			() -> config.apply(new FeedbackConfigs().withRotorToSensorRatio(1 / 64.0)
				.withSensorToMechanismRatio(1 / 64.0)
				.withFeedbackRemoteSensorID(SHOOTER_ENCODER)
				.withFeedbackSensorSource(FeedbackSensorSourceValue.RemoteCANcoder))
		);
		return result;
	}

	public boolean atPosition() {
		return Math.abs(getMeasurement() - getSetpoint()) <= ERROR;
	}

	public static enum Position implements MotorSubsystem.Position {
		TOP_HARD_STOP(0),
		SUBWOOFER_FRONT(0),
		SUBWOOFER_SIDE(0),
		PODIUM(0),
		BOTTOM_HARD_STOP(0);
		private final int pos;
		Position(int pos) {
			this.pos = pos;
		}
		@Override
		public double getPos() {
			return pos;
		}
	}
}
