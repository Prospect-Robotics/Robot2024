package com.team2813.subsytems;

import com.ctre.phoenix.motorcontrol.TalonFXInvertType;

//import com.team2813.lib2813.control.motors;
import com.team2813.lib2813.control.motors.TalonFXWrapper;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Magazine extends SubsystemBase {
    private final TalonFXWrapper intakeMotor = new TalonFXWrapper(LEFT_MAGAZINE, TalonFXInvertType.Clockwise);
    private final TalonFXWrapper pivotMotor = new TalonFXWrapper(RIGHT_MAGAZINE, TalonFXInvertType.CounterClockwise);
}