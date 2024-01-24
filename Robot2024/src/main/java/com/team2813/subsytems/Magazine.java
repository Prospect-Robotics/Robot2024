package com.team2813.subsytems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;

import com.revrobotics.CANSparkLowLevel;
import com.team2813.lib2813.control.motors.TalonFXWrapper;
//import com.team2813.lib2813.control.motors.SparkMaxWrapper;
import com.team2813.lib2813.control.ControlMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static com.team2813.Constants.*;
public class Magazine extends SubsystemBase {
    private final TalonFXWrapper leftMagMotor = new TalonFXWrapper(LEFT_MAGAZINE, TalonFXInvertType.Clockwise);
    private final TalonFXWrapper rightMagMotor = new TalonFXWrapper(RIGHT_MAGAZINE, TalonFXInvertType.Clockwise);

    //private final SparkMaxWrapper leftMagMotor = new SparkMaxWrapper(LEFT_MAGAZINE, CANSparkLowLevel.MotorType.kBrushless, true);
    //private final SparkMaxWrapper rightMagMotor = new SparkMaxWrapper(RIGHT_MAGAZINE, CANSparkLowLevel.MotorType.kBrushless, true);

    public Magazine() {


    }

    public void magazine() { leftMagMotor.set(ControlMode.DUTY_CYCLE, 0.5); rightMagMotor.set(ControlMode.DUTY_CYCLE, 0.5); }
}