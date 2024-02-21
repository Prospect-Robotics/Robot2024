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
		ampMotor.set(ControlMode.DUTY_CYCLE, 0.7);
	}
	
    //Motor runs clockwise - pushes out Note
    public void pushNoteOut() {
        ampMotor.set(ControlMode.DUTY_CYCLE, -0.5);
    }

    public void stop() {
        ampMotor.set(ControlMode.DUTY_CYCLE, 0);
    }
}