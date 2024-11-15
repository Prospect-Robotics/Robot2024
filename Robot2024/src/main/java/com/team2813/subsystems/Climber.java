package com.team2813.subsystems;

import static com.team2813.Constants.CLIMBER;

import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.PIDMotor;
import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.util.ConfigUtils;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
	private final PIDMotor climberMotor;

	public Climber() {
		TalonFXWrapper climberMotor = new TalonFXWrapper(CLIMBER, InvertType.CLOCKWISE);
		climberMotor.motor().setNeutralMode(NeutralModeValue.Brake);
		TalonFXConfigurator cnf = climberMotor.motor().getConfigurator();
		ConfigUtils.phoenix6Config(
				() -> cnf.apply(
					new SoftwareLimitSwitchConfigs()
					.withForwardSoftLimitEnable(true)
					.withForwardSoftLimitThreshold(72.926758)
					.withReverseSoftLimitEnable(true)
					.withReverseSoftLimitThreshold(0.556152)
				)
		);
		this.climberMotor = climberMotor;
	}

	public void extend() {
		climberMotor.set(ControlMode.DUTY_CYCLE, 0.7);
	}

	public void retract() {
		climberMotor.set(ControlMode.DUTY_CYCLE, -0.3);
	}

	public void stop() {
		climberMotor.set(ControlMode.DUTY_CYCLE, -0.01);
	}

	// @Override
	public void outputTelemetry() {
		SmartDashboard.putNumber("Climber Encoder", climberMotor.position());
		SmartDashboard.putNumber("Climber Velocity", climberMotor.getVelocity());
	}
}
