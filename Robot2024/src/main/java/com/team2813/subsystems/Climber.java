package com.team2813.subsystems;

import static com.team2813.Constants.CLIMBER;

import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.PIDMotor;
import com.team2813.lib2813.control.motors.TalonFXWrapper;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
    private final PIDMotor climberMotor = new TalonFXWrapper(CLIMBER, InvertType.CLOCKWISE);

    public void extend() {
        // TODO: Find out proper demand value for extending
        climberMotor.set(ControlMode.DUTY_CYCLE, 0.7);
    }

    public void retract() {
        // TODO: Find out proper demand value for retracting
        climberMotor.set(ControlMode.DUTY_CYCLE, -0.3);
    }

	@Override
	public void periodic() {
	    SmartDashboard.putNumber("Climber Encoder", climberMotor.position());
        SmartDashboard.putNumber("Climber Velocity", climberMotor.getVelocity());
	}
}

