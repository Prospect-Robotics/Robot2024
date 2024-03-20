package com.team2813.subsystems;

import static com.team2813.Constants.SHOOTER_1;
import static com.team2813.Constants.SHOOTER_2;

import java.util.function.Supplier;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.PIDMotor;
import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.util.ConfigUtils;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
	PIDMotor shooterMotor;
	private double targetVelocity;
	private Supplier<Double> voltage;
	public Shooter() {
		TalonFXWrapper m = new TalonFXWrapper(SHOOTER_2, InvertType.CLOCKWISE);
		m.addFollower(SHOOTER_1, InvertType.FOLLOW_MASTER);
		TalonFXConfigurator config = m.motor().getConfigurator();
		ConfigUtils.phoenix6Config(
				() -> config.apply(new FeedbackConfigs().withSensorToMechanismRatio(24.0 / 36)
				));
		ConfigUtils.phoenix6Config(
			() -> config.apply(
					new Slot0Configs().withKP(0.01).withKS(0).withKV(0.009))
		);
		ConfigUtils.phoenix6Config(
			() -> config.apply(new VoltageConfigs().withPeakForwardVoltage(4.5))
		);
		ConfigUtils.phoenix6Config(
			() -> config.apply(new CurrentLimitsConfigs().withSupplyCurrentLimit(40))
		);
		
		shooterMotor = m;
		voltage = m.motor().getMotorVoltage().asSupplier();
	}

	@Override
	public void periodic() {
		super.periodic();
		SmartDashboard.putNumber("Shooter Velocity", shooterMotor.getVelocity());
		SmartDashboard.putNumber("Shooter Target Velocity", targetVelocity);
		SmartDashboard.putNumber("Shooter Voltage Usage", voltage.get());
	}

	public boolean atVelocity() {
		return Math.abs(targetVelocity - shooterMotor.getVelocity()) < 1;
	}

	public void stop() {
		targetVelocity = 0;
		shooterMotor.set(ControlMode.VELOCITY, 0);
	}

	public void run(double demand) {
		targetVelocity = demand;
		shooterMotor.set(ControlMode.VELOCITY, demand);
	}
}
