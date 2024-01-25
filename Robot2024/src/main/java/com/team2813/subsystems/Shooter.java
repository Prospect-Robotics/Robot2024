package com.team2813.subsystems;

import com.team2813.lib2813.control.Motor;
import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.subsystems.MotorSubsystem;
import static com.team2813.Constants.*;

import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;

public class Shooter extends MotorSubsystem<Shooter.Angle> {
	Motor shooterMotor;
	public Shooter() {
		// TODO: fix invert type
		super(new MotorSubsystemConfiguration(
			new TalonFXWrapper(SHOOTER_PIVOT, TalonFXInvertType.Clockwise),
			null //TODO: write encoder class (embarassing wow)
			));
		TalonFXWrapper m = new TalonFXWrapper(SHOOTER_1, TalonFXInvertType.Clockwise);
		m.addFollower(SHOOTER_2, TalonFXInvertType.OpposeMaster);
		shooterMotor = m;
	}
	public static enum Angle implements MotorSubsystem.Position {
		TEST(0.0);
		private double pos;
		Angle(double pos) {
			this.pos = pos;
		}
		@Override
		public double getPos() {
			return pos;
		}
	}
}
