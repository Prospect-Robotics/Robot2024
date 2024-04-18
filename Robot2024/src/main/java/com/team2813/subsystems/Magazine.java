package com.team2813.subsystems;

import static com.team2813.Constants.KICKER;
import static com.team2813.Constants.MAGAZINE;

import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.Motor;
import com.team2813.lib2813.control.PIDMotor;
import com.team2813.lib2813.control.motors.TalonFXWrapper;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
public class Magazine extends SubsystemBase {
    private final Motor kickerMotor;
	private final PIDMotor magMotor;
	private final DigitalInput beamBreak;

    public Magazine() {
		beamBreak = new DigitalInput(0);
		kickerMotor = new TalonFXWrapper(KICKER, InvertType.COUNTER_CLOCKWISE);

		magMotor = new TalonFXWrapper(MAGAZINE, InvertType.COUNTER_CLOCKWISE);
		Shuffleboard.getTab("swerve").addBoolean("note in mag", this::noteInMag);
    }

	public boolean noteInMag() {
		return !beamBreak.get();
	}

	//Runs Kicker and Magazine Motor together
    public void runMagKicker() {
		kickerMotor.set(ControlMode.DUTY_CYCLE, 0.8);
		magMotor.set(ControlMode.DUTY_CYCLE, 0.8);
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
		kickerMotor.set(ControlMode.DUTY_CYCLE, -0.6);
	}
}