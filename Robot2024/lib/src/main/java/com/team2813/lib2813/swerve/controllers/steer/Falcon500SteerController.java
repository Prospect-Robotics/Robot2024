package com.team2813.lib2813.swerve.controllers.steer;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.swervedrivespecialties.swervelib.Mk4ModuleConfiguration;
import com.swervedrivespecialties.swervelib.ModuleConfiguration;
import com.swervedrivespecialties.swervelib.SteerController;
import com.swervedrivespecialties.swervelib.ctre.CanCoderAbsoluteConfiguration;
import com.team2813.lib2813.util.ConfigUtils;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;

public class Falcon500SteerController implements SteerController {
    private static final int ENCODER_RESET_ITERATIONS = 500;
    private static final double ENCODER_RESET_MAX_ANGULAR_VELOCITY = Math.toRadians(0.5);

    private TalonFX motor;
    private final SteerEncoder absoluteEncoder;
    private final double sensorPositionCoefficient;
    private final double sensorVelocityCoefficient;
    private final TalonFXControlMode controlMode = TalonFXControlMode.Position;

    private double referenceAngleRadians = 0;
    private double resetIteration = 0;

    public Falcon500SteerController(
            Falcon500SteerConfiguration steerConfiguration,
            ModuleConfiguration moduleConfiguration,
            Mk4ModuleConfiguration mk4Configuration
    ) {
        CanCoderAbsoluteConfiguration absoluteEncoderConfig = steerConfiguration.getEncoderConfiguration();

		CANCoderConfiguration config = new CANCoderConfiguration();
		config.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
		config.magnetOffsetDegrees = Math.toDegrees(absoluteEncoderConfig.getOffset());
		config.sensorDirection = false;

		CANCoder cancoder = new CANCoder(absoluteEncoderConfig.getId(), steerConfiguration.getCanbus());
		ConfigUtils.ctreConfig(() -> cancoder.configAllSettings(config, 250));

		absoluteEncoder = new SteerEncoder(cancoder);

		sensorPositionCoefficient = 2 * Math.PI / 2048 * moduleConfiguration.getSteerReduction();
		sensorVelocityCoefficient = sensorPositionCoefficient * 10;

		TalonFXConfiguration motorConfiguration = new TalonFXConfiguration();
		motorConfiguration.slot0.kP = 0.2;
		motorConfiguration.slot0.kD = 0.1;

		motorConfiguration.voltageCompSaturation = mk4Configuration.getNominalVoltage();

		motorConfiguration.supplyCurrLimit.currentLimit = mk4Configuration.getSteerCurrentLimit();
		motorConfiguration.supplyCurrLimit.enable = true;

		motor = new TalonFX(steerConfiguration.getMotorPort(), steerConfiguration.getCanbus());
		ConfigUtils.ctreConfig(() -> motor.configAllSettings(motorConfiguration, 250));

		motor.enableVoltageCompensation(true);

		ConfigUtils.ctreConfig(() -> motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 250));
		motor.setSensorPhase(true);
		motor.setInverted(moduleConfiguration.isSteerInverted() ? TalonFXInvertType.CounterClockwise : TalonFXInvertType.Clockwise);
		motor.setNeutralMode(NeutralMode.Brake);

		ConfigUtils.ctreConfig(() -> motor.setSelectedSensorPosition(absoluteEncoder.getAbsoluteAngle() / sensorPositionCoefficient, 0, 250));

		// Config status frame rates if not using CANivore (CAN FD sets optimal rates already)
		if (steerConfiguration.getCanbus().equals("")) {
			ConfigUtils.ctreConfig(() -> motor.setStatusFramePeriod(StatusFrameEnhanced.Status_21_FeedbackIntegrated,
					250,
					250));
			ConfigUtils.ctreConfig(() -> motor.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat,
					125,
					250));
		}
    }

    @Override
    public double getReferenceAngle() {
        return referenceAngleRadians;
    }

    @Override
    public void setReferenceAngle(double referenceAngleRadians) {
        double currentAngleRadians;
        currentAngleRadians = motor.getSelectedSensorPosition() * sensorPositionCoefficient;

        // Reset the motor's encoder periodically when the module is not rotating.
        // Sometimes (~5% of the time) when we initialize, the absolute encoder isn't fully set up, and we don't
        // end up getting a good reading. If we reset periodically this won't matter anymore.
		if (motor.getSelectedSensorVelocity() * sensorVelocityCoefficient < ENCODER_RESET_MAX_ANGULAR_VELOCITY) {
			if (++resetIteration >= ENCODER_RESET_ITERATIONS) {
				resetIteration = 0;
				double absoluteAngle = absoluteEncoder.getAbsoluteAngle();
				motor.setSelectedSensorPosition(absoluteAngle / sensorPositionCoefficient);
				currentAngleRadians = absoluteAngle;
			}
		} else {
			resetIteration = 0;
		}

        double currentAngleRadiansMod = currentAngleRadians % (2.0 * Math.PI);
        if (currentAngleRadiansMod < 0.0) {
            currentAngleRadiansMod += 2.0 * Math.PI;
        }

        // The reference angle has the range [0, 2pi) but the Falcon's encoder can go above that
        double adjustedReferenceAngleRadians = referenceAngleRadians + currentAngleRadians - currentAngleRadiansMod;
        if (referenceAngleRadians - currentAngleRadiansMod > Math.PI) {
            adjustedReferenceAngleRadians -= 2.0 * Math.PI;
        } else if (referenceAngleRadians - currentAngleRadiansMod < -Math.PI) {
            adjustedReferenceAngleRadians += 2.0 * Math.PI;
        }

		motor.set(controlMode, adjustedReferenceAngleRadians / sensorPositionCoefficient);

        this.referenceAngleRadians = referenceAngleRadians;
    }

    @Override
    public double getStateAngle() {
        double motorAngleRadians;
        motorAngleRadians = motor.getSelectedSensorPosition() * sensorPositionCoefficient;

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

		container.addNumber("Steer Motor Temp (degrees Celsius)", motor::getTemperature);
    }
}