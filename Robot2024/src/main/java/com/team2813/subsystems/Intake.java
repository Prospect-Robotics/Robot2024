package com.team2813.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;

import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.control.ControlMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static com.team2813.Constants.*;
public class Intake extends SubsystemBase {
    private final TalonFXWrapper intakeMotor = new TalonFXWrapper(INTAKE, TalonFXInvertType.CounterClockwise);
    private final TalonFXWrapper pivotMotor = new TalonFXWrapper(INTAKE_PIVOT, TalonFXInvertType.CounterClockwise);

    public Intake() {

    }
}


