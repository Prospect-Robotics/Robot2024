package com.team2813.subsystems;

import static com.team2813.Constants.SHOOTER_ENCODER;
import static com.team2813.Constants.SHOOTER_PIVOT;

import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.PIDMotor;
import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.subsystems.MotorSubsystem;
import com.team2813.lib2813.util.ConfigUtils;

public class ShooterPivot extends MotorSubsystem<ShooterPivot.Position> {
	private static final double ERROR = 0.01;

	public ShooterPivot() {
		super(new MotorSubsystemConfiguration(
				pivotMotor()).acceptableError(ERROR)
				.PID(0.2, 0, 0));
	}

	private static PIDMotor pivotMotor() {
		TalonFXWrapper result = new TalonFXWrapper(SHOOTER_PIVOT, InvertType.CLOCKWISE);
		result.setNeutralMode(NeutralModeValue.Brake);
		TalonFXConfigurator config = result.motor().getConfigurator();
		ConfigUtils.phoenix6Config(
				() -> config.apply(new FeedbackConfigs().withRotorToSensorRatio(-64/24)
						.withSensorToMechanismRatio(-20 / 1)
						.withFeedbackRemoteSensorID(SHOOTER_ENCODER)
						.withFeedbackSensorSource(FeedbackSensorSourceValue.RemoteCANcoder)));
		ConfigUtils.phoenix6Config(
				() -> config.apply(new SoftwareLimitSwitchConfigs()
						.withReverseSoftLimitThreshold(Position.TOP_HARD_STOP.getPos())
						.withForwardSoftLimitThreshold(Position.BOTTOM_HARD_STOP.getPos())
						.withForwardSoftLimitEnable(true)
						.withReverseSoftLimitEnable(true)));

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
		TEST(0.067871),
		BOTTOM_HARD_STOP(0.111816);

		private final double pos;

		Position(double pos) {
			this.pos = pos;
		}

		@Override
		public double getPos() {
			return pos;
		}
	}
}
