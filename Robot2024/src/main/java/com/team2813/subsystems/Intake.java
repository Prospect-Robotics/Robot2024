package com.team2813.subsystems;

import static com.team2813.Constants.*;

import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.Encoder;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.Motor;
import com.team2813.lib2813.control.encoders.CancoderWrapper;
import com.team2813.lib2813.control.motors.TalonFXWrapper;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
public class Intake extends SubsystemBase {
    private final Motor intakeMotor = new TalonFXWrapper(INTAKE, InvertType.COUNTER_CLOCKWISE);
    private final Motor pivotMotor = new TalonFXWrapper(INTAKE_PIVOT, InvertType.COUNTER_CLOCKWISE);
    private final Encoder encoder = new CancoderWrapper(INTAKE_ENCODER);

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

    public double getMeasurement() { return encoder.position(); }

    public void zeroSensors() { encoder.setPosition(0);}



}


