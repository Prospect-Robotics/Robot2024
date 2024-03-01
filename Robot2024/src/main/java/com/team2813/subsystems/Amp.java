package com.team2813.subsystems;

import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.Motor;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static com.team2813.Constants.*;
public class Amp extends SubsystemBase {
    private final Motor ampMotor;

    public Amp() {
		ampMotor = new TalonFXWrapper(AMP, InvertType.COUNTER_CLOCKWISE);
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
}