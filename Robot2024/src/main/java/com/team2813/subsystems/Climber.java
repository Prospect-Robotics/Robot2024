package com.team2813.subsystems;

import static com.team2813.Constants.CLIMBER;

import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.control.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

@SuppressWarnings("unused")
public class Climber extends SubsystemBase {
    private final TalonFXWrapper climberMotor = new TalonFXWrapper(CLIMBER, TalonFXInvertType.Clockwise);
    
    public Climber() {}

    public void extend() {
        climberMotor.set(ControlMode.DUTY_CYCLE, 0.7); //TODO: Find out proper demand value for extending
    }

    public void retract() {
        climberMotor.set(ControlMode.DUTY_CYCLE, -0.3); //TODO: Find out proper demand value for retracting
    }

    // @Override
    public void outputTelemetry() {
        SmartDashboard.putNumber("Climber Encoder", climberMotor.position());
        // SmartDashboard.putNumber("Climber Velocity", climberMotor.getVelocity());
        // TODO: Find out where to get motor velocity for TalonFX motors
    }
}

