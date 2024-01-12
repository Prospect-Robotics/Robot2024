package com.team2813.lib.swerve.controllers.steer;

import com.swervedrivespecialties.swervelib.ctre.CanCoderAbsoluteConfiguration;

public class SteerConfiguration {

    private final int motorPort;

    private final CanCoderAbsoluteConfiguration encoderConfiguration;

    public SteerConfiguration(int motorPort, CanCoderAbsoluteConfiguration encoderConfiguration) {
        this.motorPort = motorPort;
        this.encoderConfiguration = encoderConfiguration;
    }

    public int getMotorPort() {
        return motorPort;
    }

    public CanCoderAbsoluteConfiguration getEncoderConfiguration() {
        return encoderConfiguration;
    }
}