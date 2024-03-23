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

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterPivot extends MotorSubsystem<ShooterPivot.Position> {

	public ShooterPivot() {
		super(new MotorSubsystemConfiguration(
				pivotMotor()).acceptableError(0.004)
				.PID(4.2, 0, 0).startingPosition(Position.TOP_HARD_STOP));
		SmartDashboard.putData("Shooter Pivot PID", m_controller);
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

		result.setPosition(Position.TOP_HARD_STOP.getPos());

		return result;
	}

	@Override
	protected void useOutput(double output, double setpoint) {
		if (output < 0) {
			output -= 0.15;
		}
		super.useOutput(output, setpoint);
	}

	@Override
	public void periodic() {
		super.periodic();
		SmartDashboard.putNumber("Shoooter Pivot CANCoder Position", encoder.position());
	}

	public static enum Position implements MotorSubsystem.Position {
		TOP_HARD_STOP(0),
		SUBWOOFER_FRONT(0.023926),
		SUBWOOFER_SIDE(0.023926),
		AMP(0.033936),
		PODIUM(0.054443),
		TEST(0.067871),
		FAR_SPEAKER(0.088135),
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
