package com.team2813.lib2813.swerve.controllers.drive;

import com.revrobotics.*;
import com.swervedrivespecialties.swervelib.Mk4ModuleConfiguration;
import com.swervedrivespecialties.swervelib.ModuleConfiguration;
import com.team2813.lib2813.util.ConfigUtils;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;

public class NeoDriveController implements DriveController {
    private final CANSparkMax motor;
    private final RelativeEncoder encoder;
    private final SparkPIDController pidController;
    private final double maxVelocity;

    private SimpleMotorFeedforward feedforward;
    private boolean hasPidConstants = false;

    public NeoDriveController(int id, ModuleConfiguration moduleConfiguration, Mk4ModuleConfiguration mk4Configuration) {
        maxVelocity = 5676.0 / 60.0 * moduleConfiguration.getDriveReduction() * moduleConfiguration.getWheelDiameter();

        motor = new CANSparkMax(id, CANSparkLowLevel.MotorType.kBrushless);
        motor.setInverted(moduleConfiguration.isDriveInverted());

        ConfigUtils.revConfig(() -> motor.enableVoltageCompensation(mk4Configuration.getNominalVoltage()));

        ConfigUtils.revConfig(() -> motor.setSmartCurrentLimit((int) mk4Configuration.getDriveCurrentLimit()));

        ConfigUtils.revConfig(() -> motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus0, 100));
        ConfigUtils.revConfig(() -> motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus1, 20));
        ConfigUtils.revConfig(() -> motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus2, 20));

        motor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        encoder = motor.getEncoder();
        double positionConversionFactor = Math.PI * moduleConfiguration.getWheelDiameter() * moduleConfiguration.getDriveReduction();
        encoder.setPositionConversionFactor(positionConversionFactor);
        encoder.setVelocityConversionFactor(positionConversionFactor / 60);

        pidController = motor.getPIDController();
    }

    @Override
    public NeoDriveController withPidConstants(double proportional, double integral, double derivative) {
        hasPidConstants = true;

        ConfigUtils.revConfig(() -> pidController.setP(proportional));
        ConfigUtils.revConfig(() -> pidController.setI(integral));
        ConfigUtils.revConfig(() -> pidController.setD(derivative));

        return this;
    }

    @Override
    public boolean hasPidConstants() {
        return hasPidConstants;
    }

    @Override
    public NeoDriveController withFeedforward(SimpleMotorFeedforward feedforward) {
        this.feedforward = feedforward;
        return this;
    }

    @Override
    public boolean hasFeedForward() {
        return feedforward != null;
    }

    /**
     * @param velocity desired velocity in m/s
     */
    @Override
    public void setReferenceVelocity(double velocity) {
        if (hasPidConstants()) {
            if (hasFeedForward()) {
                pidController.setReference(velocity, CANSparkMax.ControlType.kVelocity, 0, feedforward.calculate(velocity));
            }
            else {
                pidController.setReference(velocity, CANSparkMax.ControlType.kVelocity);
            }
        }
        else {
            double dutyCycle = velocity / maxVelocity;
            pidController.setReference(dutyCycle, CANSparkMax.ControlType.kDutyCycle);
        }
    }

    @Override
    public double getDistanceDriven() {
        return encoder.getPosition();
    }

    @Override
    public double getStateVelocity() {
        return encoder.getVelocity();
    }

    @Override
    public void resetEncoder() {
        encoder.setPosition(0);
    }

    @Override
    public void addDashboardEntries(ShuffleboardContainer container) {
        DriveController.super.addDashboardEntries(container);
        container.addNumber("Drive Motor Temp (degrees Celsius)", motor::getMotorTemperature);
    }
}