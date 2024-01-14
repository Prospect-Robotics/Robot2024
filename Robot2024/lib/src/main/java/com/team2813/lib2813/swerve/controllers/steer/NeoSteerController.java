package com.team2813.lib2813.swerve.controllers.steer;

import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.CANCoderStatusFrame;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.swervedrivespecialties.swervelib.AbsoluteEncoder;
import com.swervedrivespecialties.swervelib.Mk4ModuleConfiguration;
import com.swervedrivespecialties.swervelib.ModuleConfiguration;
import com.swervedrivespecialties.swervelib.SteerController;
import com.swervedrivespecialties.swervelib.ctre.CanCoderAbsoluteConfiguration;
import com.team2813.lib2813.util.ConfigUtils;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;

public class NeoSteerController implements SteerController {
    private static final int ENCODER_RESET_ITERATIONS = 500;
    private static final double ENCODER_RESET_MAX_ANGULAR_VELOCITY = Math.toRadians(0.5);

    private final CANSparkMax motor;
    private final SparkPIDController controller;
    private final RelativeEncoder motorEncoder;
    private final AbsoluteEncoder absoluteEncoder;

    private double referenceAngleRadians = 0;

    private double resetIteration = 0;

    public NeoSteerController(SteerConfiguration steerConfiguration, ModuleConfiguration moduleConfiguration, Mk4ModuleConfiguration mk4ModuleConfiguration) {
        CanCoderAbsoluteConfiguration absoluteEncoderConfig = steerConfiguration.getEncoderConfiguration();

        CANCoderConfiguration config = new CANCoderConfiguration();
        config.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
        config.magnetOffsetDegrees = Math.toDegrees(absoluteEncoderConfig.getOffset());
        config.sensorDirection = false;

        CANCoder cancoder = new CANCoder(absoluteEncoderConfig.getId());
        ConfigUtils.ctreConfig(() -> cancoder.configAllSettings(config, 250));

        ConfigUtils.ctreConfig(() -> cancoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData, 10, 250));

        absoluteEncoder = new SteerEncoder(cancoder);

        motor = new CANSparkMax(steerConfiguration.getMotorPort().getCanId(), CANSparkLowLevel.MotorType.kBrushless);
        ConfigUtils.revConfig(() -> motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus0, 100));
        ConfigUtils.revConfig(() -> motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus1, 20));
        ConfigUtils.revConfig(() -> motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus2, 20));
        ConfigUtils.revConfig(() -> motor.setIdleMode(CANSparkMax.IdleMode.kBrake));
        motor.setInverted(!moduleConfiguration.isSteerInverted());

        ConfigUtils.revConfig(() -> motor.enableVoltageCompensation(mk4ModuleConfiguration.getNominalVoltage()));

        ConfigUtils.revConfig(() -> motor.setSmartCurrentLimit((int) Math.round(mk4ModuleConfiguration.getSteerCurrentLimit())));

        motorEncoder = motor.getEncoder();
        ConfigUtils.revConfig(() -> motorEncoder.setPositionConversionFactor(2.0 * Math.PI * moduleConfiguration.getSteerReduction()));
        ConfigUtils.revConfig(() -> motorEncoder.setVelocityConversionFactor(2.0 * Math.PI * moduleConfiguration.getSteerReduction() / 60.0));
        ConfigUtils.revConfig(() -> motorEncoder.setPosition(absoluteEncoder.getAbsoluteAngle()));

        controller = motor.getPIDController();
        ConfigUtils.revConfig(() -> controller.setP(1));
        ConfigUtils.revConfig(() -> controller.setD(0.1));

        ConfigUtils.revConfig(() -> controller.setFeedbackDevice(motorEncoder));
    }

    @Override
    public double getReferenceAngle() {
        return referenceAngleRadians;
    }

    @Override
    public void setReferenceAngle(double referenceAngleRadians) {
        double currentAngleRadians = motorEncoder.getPosition();

        // Reset the NEO's encoder periodically when the module is not rotating.
        // Sometimes (~5% of the time) when we initialize, the absolute encoder isn't fully set up, and we don't
        // end up getting a good reading. If we reset periodically this won't matter anymore.
        if (motorEncoder.getVelocity() < ENCODER_RESET_MAX_ANGULAR_VELOCITY) {
            if (++resetIteration >= ENCODER_RESET_ITERATIONS) {
                resetIteration = 0;
                double absoluteAngle = absoluteEncoder.getAbsoluteAngle();
                motorEncoder.setPosition(absoluteAngle);
                currentAngleRadians = absoluteAngle;
            }
        } else {
            resetIteration = 0;
        }

        double currentAngleRadiansMod = currentAngleRadians % (2.0 * Math.PI);
        if (currentAngleRadiansMod < 0.0) {
            currentAngleRadiansMod += 2.0 * Math.PI;
        }

        // The reference angle has the range [0, 2pi) but the Neo's encoder can go above that
        double adjustedReferenceAngleRadians = referenceAngleRadians + currentAngleRadians - currentAngleRadiansMod;
        if (referenceAngleRadians - currentAngleRadiansMod > Math.PI) {
            adjustedReferenceAngleRadians -= 2.0 * Math.PI;
        } else if (referenceAngleRadians - currentAngleRadiansMod < -Math.PI) {
            adjustedReferenceAngleRadians += 2.0 * Math.PI;
        }

        this.referenceAngleRadians = referenceAngleRadians;

        controller.setReference(adjustedReferenceAngleRadians, CANSparkMax.ControlType.kPosition);
    }

    @Override
    public double getStateAngle() {
        double motorAngleRadians = motorEncoder.getPosition();
        motorAngleRadians %= 2.0 * Math.PI;
        if (motorAngleRadians < 0.0) {
            motorAngleRadians += 2.0 * Math.PI;
        }

        return motorAngleRadians;
    }

    public void addDashboardEntries(ShuffleboardContainer container) {
        container.addNumber("Current Angle", () -> Math.toDegrees(getStateAngle()));
        container.addNumber("Target Angle", () -> Math.toDegrees(getReferenceAngle()));

        container.addNumber("Absolute Encoder Angle", () -> Math.toDegrees(absoluteEncoder.getAbsoluteAngle()));

        container.addNumber("Steer Motor Temp (degrees Celsius)", motor::getMotorTemperature);
    }
}