package com.team2813.subsystems;

import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.Motor;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static com.team2813.Constants.*;
public class Magazine extends SubsystemBase {
    Motor kickerMotor;

    public Magazine() {
		TalonFXWrapper kickerMotor = new TalonFXWrapper(KICKER, TalonFXInvertType.CounterClockwise);
		kickerMotor.addFollower(MAGAZINE, TalonFXInvertType.FollowMaster);
		this.kickerMotor = kickerMotor;
    }

    public void run() {
		kickerMotor.set(ControlMode.DUTY_CYCLE, 0.5);
	}

	public void stop() {
		kickerMotor.set(ControlMode.DUTY_CYCLE, 0);
	}
}