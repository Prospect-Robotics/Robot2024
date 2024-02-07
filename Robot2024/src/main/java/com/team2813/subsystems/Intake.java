package com.team2813.subsystems;

import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkLowLevel;

import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.control.motors.SparkMaxWrapper;
import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.util.ConfigUtils;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static com.team2813.Constants.*;
public class Intake extends SubsystemBase {
    private final TalonFXWrapper intakeMotor = new TalonFXWrapper(INTAKE, TalonFXInvertType.CounterClockwise);
    private final TalonFXWrapper pivotMotor = new TalonFXWrapper(INTAKE_PIVOT, TalonFXInvertType.CounterClockwise);
    private final CANCoder encoder = new CANCoder(INTAKE_ENCODER);

    //private final SparkMaxWrapper intakeMotor = new SparkMaxWrapper(INTAKE, CANSparkLowLevel.MotorType.kBrushless, true);
    //private final SparkMaxWrapper pivotMotor = new SparkMaxWrapper(INTAKE_PIVOT, CANSparkLowLevel.MotorType.kBrushless, true);

    private static final double INTAKE_SPEED = 0.25;
    private static final double OUTTAKE_SPEED = -0.25;

    private static final double PIVOT_UP_SPEED = .10;
    private static final double PIVOT_DOWN_SPEED = -.10;


    public Intake() {
        //encoder.configSensorDirection(true);
    }

    public void intakeNote() { intakeMotor.set(ControlMode.DUTY_CYCLE, INTAKE_SPEED); }

    public void outtakeNote() { intakeMotor.set(ControlMode.DUTY_CYCLE, OUTTAKE_SPEED); }

    public void stopIntakeMotor() { intakeMotor.set(ControlMode.DUTY_CYCLE, 0); }

    public void pivotUp() { pivotMotor.set(ControlMode.DUTY_CYCLE, PIVOT_UP_SPEED); }

    public void pivotDown() { pivotMotor.set(ControlMode.DUTY_CYCLE, PIVOT_DOWN_SPEED); }

    public void stopPivotMotor() { pivotMotor.set(ControlMode.DUTY_CYCLE, 0); }

    public double getMeasurement() { return encoder.getPosition(); }

    public void zeroSensors() { ConfigUtils.ctreConfig(() -> encoder.setPosition(0)); }



}


