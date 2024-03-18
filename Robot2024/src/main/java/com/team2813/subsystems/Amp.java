package com.team2813.subsystems;

import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.Motor;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static com.team2813.Constants.*;
public class Amp extends SubsystemBase {
    Motor ampMotor;

    public Amp() {
		TalonFXWrapper ampMotor = new TalonFXWrapper(AMP, InvertType.COUNTER_CLOCKWISE);
		this.ampMotor = ampMotor;
    }

    //Motor runs counterclockwise - takes in Note
    public void pushNoteIn() {
		ampMotor.set(ControlMode.DUTY_CYCLE, 0.2);
	}
	
    //Motor runs clockwise - pushes out Note
    public void pushNoteOut() {
        ampMotor.set(ControlMode.DUTY_CYCLE, -0.4);
    }

    public void stop() {
        ampMotor.set(ControlMode.DUTY_CYCLE, 0);
    }




    private final Motor intakeMotor = new TalonFXWrapper(INTAKE, InvertType.COUNTER_CLOCKWISE);

    private static final double INTAKE_SPEED = 0.1;
    private static final double OUTTAKE_SPEED = -0.5;

    public void ampIntake() { intakeMotor.set(ControlMode.DUTY_CYCLE, INTAKE_SPEED); }

    public void ampOuttake() { intakeMotor.set(ControlMode.DUTY_CYCLE, OUTTAKE_SPEED); }

    public void ampStop() { intakeMotor.set(ControlMode.DUTY_CYCLE, 0); }




}

/*package com.team2813.subsystems;

import static com.team2813.Constants.INTAKE;

import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.Motor;
import com.team2813.lib2813.control.motors.TalonFXWrapper;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
public class Amp extends SubsystemBase {
    private final Motor intakeMotor = new TalonFXWrapper(INTAKE, InvertType.COUNTER_CLOCKWISE);

    private static final double INTAKE_SPEED = 0.1;
    private static final double OUTTAKE_SPEED = -0.5;

    public Amp() {

    }
    public void ampIntake() { intakeMotor.set(ControlMode.DUTY_CYCLE, INTAKE_SPEED); }

    public void ampOuttake() { intakeMotor.set(ControlMode.DUTY_CYCLE, OUTTAKE_SPEED); }

    public void ampStop() { intakeMotor.set(ControlMode.DUTY_CYCLE, 0); }
}*/


