package com.team2813.subsystems;

import static com.team2813.Constants.INTAKE;

import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.Motor;
import com.team2813.lib2813.control.motors.TalonFXWrapper;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
public class Intake extends SubsystemBase {
    private final Motor intakeMotor = new TalonFXWrapper(INTAKE, InvertType.COUNTER_CLOCKWISE);
    private static final double INTAKE_SPEED = 0.25;
    private static final double OUTTAKE_SPEED = -0.25;
	
    public void intake() { intakeMotor.set(ControlMode.DUTY_CYCLE, INTAKE_SPEED); }

    public void outtakeNote() { intakeMotor.set(ControlMode.DUTY_CYCLE, OUTTAKE_SPEED); }

    public void stopIntakeMotor() { intakeMotor.set(ControlMode.DUTY_CYCLE, 0); }
}


