package com.team2813.subsystems;

import static com.team2813.Constants.SHOOTER_1;
import static com.team2813.Constants.SHOOTER_2;
import static com.team2813.Constants.SHOOTER_ENCODER;
import static com.team2813.Constants.SHOOTER_PIVOT;

import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.PIDMotor;
import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.subsystems.MotorSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends MotorSubsystem<Shooter.Angle> {
	PIDMotor shooterMotor;
	private double targetVelocity;
	public Shooter() {
		super(new MotorSubsystemConfiguration(
			pivotMotor()
			));
		TalonFXWrapper m = new TalonFXWrapper(SHOOTER_2, InvertType.CLOCKWISE);
		m.addFollower(SHOOTER_1, InvertType.FOLLOW_MASTER);
		TalonFXConfigurator config = m.motor().getConfigurator();
		config.apply(new FeedbackConfigs().withSensorToMechanismRatio(36/24.0));
		config.apply(
			new Slot0Configs().withKP(0.034)
				.withKI(0.8).withKD(0.0015)
			);
		shooterMotor = m;
		setSetpoint(Angle.TEST);
	}

	@Override
	public void periodic() {
		super.periodic();
		SmartDashboard.putNumber("shooter velocity", shooterMotor.getVelocity());
		SmartDashboard.putNumber("Target Velocity", targetVelocity);
	}

	private static PIDMotor pivotMotor() {
		TalonFXWrapper result = new TalonFXWrapper(SHOOTER_PIVOT, InvertType.CLOCKWISE);
		result.setNeutralMode(NeutralModeValue.Brake);
		TalonFXConfigurator config = result.motor().getConfigurator();
		config.apply(new FeedbackConfigs().withRotorToSensorRatio(1 / 64.0)
		.withSensorToMechanismRatio(1 / 64.0)
		.withFeedbackRemoteSensorID(SHOOTER_ENCODER)
		.withFeedbackSensorSource(FeedbackSensorSourceValue.RemoteCANcoder));
		return result;
	}

	public void stop() {
		targetVelocity = 0;
		shooterMotor.set(ControlMode.VELOCITY, 0);
	}
	
	public void run(double demand) {
		targetVelocity = demand;
		shooterMotor.set(ControlMode.VELOCITY, demand);
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
