package com.team2813.subsystems;

import static com.team2813.Constants.INTAKE;

import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.PIDMotor;
import com.team2813.lib2813.control.motors.TalonFXWrapper;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
public class Intake extends SubsystemBase {
    private final PIDMotor intakeMotor;
	private boolean intaking = false;
    private static final double INTAKE_SPEED = 0.6;
    private static final double OUTTAKE_SPEED = -0.6;

	public Intake() {
		intakeMotor = new TalonFXWrapper(INTAKE, InvertType.COUNTER_CLOCKWISE);
	}

    public void intake() {
		intakeMotor.set(ControlMode.DUTY_CYCLE, INTAKE_SPEED);
		intaking = true;
	}

    public void outtakeNote() {
		intakeMotor.set(ControlMode.DUTY_CYCLE, OUTTAKE_SPEED);
		intaking = true;
	}

	public boolean isStalled() {
		return intaking && intakeMotor.getVelocity() < 0.01;
	}

    public void stopIntakeMotor() {
		intakeMotor.set(ControlMode.DUTY_CYCLE, 0);
		intaking = false;
	}
}


