package com.team2813.subsystems;

import static com.team2813.Constants.SHOOTER_1;
import static com.team2813.Constants.SHOOTER_2;
import static com.team2813.Constants.SHOOTER_ENCODER;
import static com.team2813.Constants.SHOOTER_PIVOT;

import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.Motor;
import com.team2813.lib2813.control.encoders.CancoderWrapper;
import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.subsystems.MotorSubsystem;

public class Shooter extends MotorSubsystem<Shooter.Angle> {
	Motor shooterMotor;
	public Shooter() {
		// TODO: fix invert type
		super(new MotorSubsystemConfiguration(
			pivotMotor(),
			new CancoderWrapper(SHOOTER_ENCODER)
			));
		TalonFXWrapper m = new TalonFXWrapper(SHOOTER_1, InvertType.CLOCKWISE);
		m.addFollower(SHOOTER_2, InvertType.FOLLOW_MASTER);
		shooterMotor = m;
		setSetpoint(Angle.TEST);
	}

	private static Motor pivotMotor() {
		TalonFXWrapper result = new TalonFXWrapper(SHOOTER_PIVOT, InvertType.CLOCKWISE);
		result.setNeutralMode(NeutralModeValue.Brake);
		TalonFXConfigurator config = result.motor().getConfigurator();
		config.apply(
			new Slot0Configs().withKG(0.4)
			.withGravityType(GravityTypeValue.Arm_Cosine)
			);
		config.apply(new FeedbackConfigs().withRotorToSensorRatio(1 / 64.0)
		.withFeedbackRemoteSensorID(SHOOTER_ENCODER));
		return result;
	}

	public void stop() {
		shooterMotor.set(ControlMode.DUTY_CYCLE, 0);
	}
	
	public void run(double demand) {
		shooterMotor.set(ControlMode.DUTY_CYCLE, demand);
	}

	public static enum Angle implements MotorSubsystem.Position {
		TEST(0.0);
		private final double pos;
		Angle(double pos) {
			this.pos = pos;
		}
		@Override
		public double getPos() {
			return pos;
		}
	}
}
