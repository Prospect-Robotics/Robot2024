package com.team2813.subsystems;

import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.Motor;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static com.team2813.Constants.*;
public class Magazine extends SubsystemBase {
    Motor kickerMotor;
	Motor magMotor;

    public Magazine() {
		kickerMotor = new TalonFXWrapper(KICKER, InvertType.COUNTER_CLOCKWISE);

		magMotor = new TalonFXWrapper(MAGAZINE, InvertType.COUNTER_CLOCKWISE);
    }

	//Runs Kicker and Magazine Motor together
    public void runMagKicker() {
		kickerMotor.set(ControlMode.DUTY_CYCLE, 0.6);
		magMotor.set(ControlMode.DUTY_CYCLE, 0.6);
	}

	public void stop() {
		kickerMotor.set(ControlMode.DUTY_CYCLE, 0);
		magMotor.set(ControlMode.DUTY_CYCLE, 0);
	}

	//Running just Magazine Motor
	public void runOnlyMag() {
		kickerMotor.set(ControlMode.DUTY_CYCLE, -0.1);
		magMotor.set(ControlMode.DUTY_CYCLE, 0.6);
	}

	public void reverseMag() {
		magMotor.set(ControlMode.DUTY_CYCLE, -0.6);
	}
}