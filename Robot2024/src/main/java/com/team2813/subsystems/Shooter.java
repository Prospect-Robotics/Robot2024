package com.team2813.subsystems;

import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.Motor;
import com.team2813.lib2813.control.encoders.CancoderWrapper;
import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.subsystems.MotorSubsystem;
import static com.team2813.Constants.*;

import com.ctre.phoenix6.controls.DutyCycleOut;

public class Shooter extends MotorSubsystem<Shooter.Angle> {
	Motor shooterMotor;
	public Shooter() {
		// TODO: fix invert type
		super(new MotorSubsystemConfiguration(
			pivotMotor(),
			new CancoderWrapper(SHOOTER_ENCODER)
			));
		TalonFXWrapper m = new TalonFXWrapper(SHOOTER_1, InvertType.CLOCKWISE);
		m.addFollower(SHOOTER_2, InvertType.OPPOSE_MASTER);
		shooterMotor = m;
		setSetpoint(Angle.TEST);
	}

	private static Motor pivotMotor() {
		TalonFXWrapper result = new TalonFXWrapper(SHOOTER_PIVOT, InvertType.CLOCKWISE);
		result.setBreakMode(true);
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
