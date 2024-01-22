package com.team2813.lib2813.swerve.controllers.drive;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.swervedrivespecialties.swervelib.Mk4ModuleConfiguration;
import com.swervedrivespecialties.swervelib.ModuleConfiguration;
import com.team2813.lib2813.util.ConfigUtils;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;

public class Falcon500DriveController implements DriveController {
    private TalonFX motor;
    private final double sensorPositionCoefficient;
    private final double sensorVelocityCoefficient;
    private final double maxVelocity;

    private SimpleMotorFeedforward feedforward;
    private boolean hasPidConstants = false;

    public Falcon500DriveController(int id, ModuleConfiguration moduleConfiguration, Mk4ModuleConfiguration mk4Configuration) {
        maxVelocity = 6380.0 / 60.0 * moduleConfiguration.getDriveReduction() * moduleConfiguration.getWheelDiameter() * Math.PI;
		TalonFXConfiguration motorConfiguration = new TalonFXConfiguration();

		sensorPositionCoefficient = Math.PI * moduleConfiguration.getWheelDiameter() * moduleConfiguration.getDriveReduction() / 2048;
		sensorVelocityCoefficient = sensorPositionCoefficient * 10;

		motorConfiguration.voltageCompSaturation = mk4Configuration.getNominalVoltage();

		motorConfiguration.supplyCurrLimit.currentLimit = mk4Configuration.getDriveCurrentLimit();
		motorConfiguration.supplyCurrLimit.enable = true;

		motor = new TalonFX(id);
		ConfigUtils.ctreConfig(() -> motor.configAllSettings(motorConfiguration));

		motor.enableVoltageCompensation(true);

		motor.setNeutralMode(NeutralMode.Brake);

		motor.setInverted(moduleConfiguration.isDriveInverted() ? TalonFXInvertType.Clockwise : TalonFXInvertType.CounterClockwise);
		motor.setSensorPhase(true);

		ConfigUtils.ctreConfig(
				() -> motor.setStatusFramePeriod(StatusFrameEnhanced.Status_21_FeedbackIntegrated, 250, 250)
		);
		ConfigUtils.ctreConfig(
				() -> motor.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat, 125, 250)
		);
    }

    public Falcon500DriveController(int id, String canbus, ModuleConfiguration moduleConfiguration, Mk4ModuleConfiguration mk4Configuration) {
        maxVelocity = 6380.0 / 60.0 * moduleConfiguration.getDriveReduction() * moduleConfiguration.getWheelDiameter() * Math.PI;
		TalonFXConfiguration motorConfiguration = new TalonFXConfiguration();

		sensorPositionCoefficient = Math.PI * moduleConfiguration.getWheelDiameter() * moduleConfiguration.getDriveReduction() / 2048;
		sensorVelocityCoefficient = sensorPositionCoefficient * 10;

		motorConfiguration.voltageCompSaturation = mk4Configuration.getNominalVoltage();

		motorConfiguration.supplyCurrLimit.currentLimit = mk4Configuration.getDriveCurrentLimit();
		motorConfiguration.supplyCurrLimit.enable = true;

		motor = new TalonFX(id, canbus);
		ConfigUtils.ctreConfig(() -> motor.configAllSettings(motorConfiguration));

		motor.enableVoltageCompensation(true);

		motor.setNeutralMode(NeutralMode.Brake);

		motor.setInverted(moduleConfiguration.isDriveInverted() ? TalonFXInvertType.Clockwise : TalonFXInvertType.CounterClockwise);
		motor.setSensorPhase(true);
    }

    @Override
    public Falcon500DriveController withPidConstants(double proportional, double integral, double derivative) {
        hasPidConstants = true;

		ConfigUtils.ctreConfig(() -> motor.config_kP(0, proportional));
		ConfigUtils.ctreConfig(() -> motor.config_kI(0, integral));
		ConfigUtils.ctreConfig(() -> motor.config_kD(0, derivative));

        return this;
    }

    @Override
    public boolean hasPidConstants() {
        return hasPidConstants;
    }

    @Override
    public Falcon500DriveController withFeedforward(SimpleMotorFeedforward feedforward) {
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
            double velocityRawUnits = velocity / sensorVelocityCoefficient;

            if (hasFeedForward()) {
				motor.set(TalonFXControlMode.Velocity, velocityRawUnits, DemandType.ArbitraryFeedForward, feedforward.calculate(velocity));
            }
            else {
                // Uncomment this if you are copying this class for a future robot
                //if (licensed) licensedMotor.setControl(new VelocityTorqueCurrentFOC(velocityRawUnits));

                // Robot-specific, do not copy! Unless...
                // the robot moves a bit after stopping while running PID
                // (and you've verified that the motors are not set to coast)
                motor.set(TalonFXControlMode.Velocity, velocityRawUnits);
            }
        }
        else {
            double dutyCycle = velocity / maxVelocity;

            motor.set(TalonFXControlMode.PercentOutput, dutyCycle);
        }
    }

    @Override
    public double getDistanceDriven() {
		return motor.getSelectedSensorPosition() * sensorPositionCoefficient;
    }

    @Override
    public double getStateVelocity() {
		return motor.getSelectedSensorVelocity() * sensorVelocityCoefficient;
    }

    @Override
    public void resetEncoder() {
        motor.setSelectedSensorPosition(0);
    }

    @Override
    public void addDashboardEntries(ShuffleboardContainer container) {
        DriveController.super.addDashboardEntries(container);
        container.addNumber("Drive Motor Temp (degrees Celsius)", motor::getTemperature);
    }
}