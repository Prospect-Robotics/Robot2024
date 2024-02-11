package com.team2813.subsystems;
import com.team2813.lib2813.control.motors.TalonFXWrapper;

import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkLowLevel;
import com.team2813.lib2813.control.Encoder;


import com.team2813.lib2813.control.motors.SparkMaxWrapper;
import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.encoders.CancoderWrapper;
import com.team2813.lib2813.util.ConfigUtils;
//import com.team2813.subsystems.Intake.Rotations;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.team2813.lib2813.subsystems.MotorSubsystem;
import com.team2813.lib2813.subsystems.MotorSubsystem.MotorSubsystemConfiguration;
import com.team2813.lib2813.control.Motor;

import static com.team2813.Constants.*;
public class IntakePivot extends MotorSubsystem<IntakePivot.Rotations> {

    private static final double PIVOT_UP_SPEED = .10;
    private static final double PIVOT_DOWN_SPEED = -.10;
    
    Motor intakePivotMotor; 
    CANCoder intakePivotEncoder;

    public IntakePivot() {
        
        super(new MotorSubsystemConfiguration(
			new TalonFXWrapper(INTAKE_PIVOT, TalonFXInvertType.CounterClockwise),
			new CancoderWrapper(INTAKE_ENCODER)
			));

        TalonFXWrapper m = new TalonFXWrapper(INTAKE_PIVOT, TalonFXInvertType.Clockwise);
        intakePivotMotor = m;

        CANCoder e = new CANCoder(INTAKE_ENCODER);
        intakePivotEncoder = e;
        setSetpoint(Rotations.INTAKE_DOWN);
    }


    public void pivotUp() { intakePivotMotor.set(ControlMode.DUTY_CYCLE, PIVOT_UP_SPEED); }

    public void pivotDown() { intakePivotMotor.set(ControlMode.DUTY_CYCLE, PIVOT_DOWN_SPEED); }

    public void stopPivotMotor() { intakePivotMotor.set(ControlMode.DUTY_CYCLE, 0); }

    public double getMeasurement() { return intakePivotEncoder.getPosition(); }

    public void zeroSensors() { ConfigUtils.ctreConfig(() -> intakePivotEncoder.setPosition(0)); }

    public boolean positionReached() {
        return Math.abs(currentPosition.getPos() - intakePivotEncoder.getPosition()) < 0.05;
    }

    public static enum Rotations implements MotorSubsystem.Position {
		INTAKE_DOWN(0.0),
        INTAKE_UP(0.0);

        Rotations(double pos) {
            this.pos = pos;
        }

        private final double pos;
        @Override
		public double getPos() {
			return pos;
		}
    }

}


