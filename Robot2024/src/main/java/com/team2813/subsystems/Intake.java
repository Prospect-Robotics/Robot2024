package com.team2813.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;

import com.revrobotics.CANSparkLowLevel;
import com.team2813.lib2813.control.motors.TalonFXWrapper;
//import com.team2813.lib2813.control.motors.SparkMaxWrapper;
import com.team2813.lib2813.control.ControlMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static com.team2813.Constants.*;
public class Intake extends SubsystemBase {
    private final TalonFXWrapper intakeMotor = new TalonFXWrapper(INTAKE, TalonFXInvertType.CounterClockwise);
    private final TalonFXWrapper pivotMotor = new TalonFXWrapper(INTAKE_PIVOT, TalonFXInvertType.CounterClockwise);
    //private final SparkMaxWrapper intakeMotor = new SparkMaxWrapper(INTAKE, CANSparkLowLevel.MotorType.kBrushless, true);
    //private final SparkMaxWrapper pivotMotor = new SparkMaxWrapper(INTAKE_PIVOT, CANSparkLowLevel.MotorType.kBrushless, true);

    public Intake() {

    }

    public void intake() { intakeMotor.set(ControlMode.DUTY_CYCLE, 0.5);}

    public void outtake() {
        intakeMotor.set(ControlMode.DUTY_CYCLE, -0.5);
    }



}


