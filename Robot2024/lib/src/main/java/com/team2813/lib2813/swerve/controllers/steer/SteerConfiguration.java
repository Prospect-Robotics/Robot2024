package com.team2813.lib2813.swerve.controllers.steer;

import com.swervedrivespecialties.swervelib.ctre.CanCoderAbsoluteConfiguration;
import com.team2813.lib2813.util.Port;

public class SteerConfiguration {

    private final Port motorPort;

    private final CanCoderAbsoluteConfiguration encoderConfiguration;

    public SteerConfiguration(Port motorPort, CanCoderAbsoluteConfiguration encoderConfiguration) {
        this.motorPort = motorPort;
        this.encoderConfiguration = encoderConfiguration;
    }

    public Port getMotorPort() {
        return motorPort;
    }

    public CanCoderAbsoluteConfiguration getEncoderConfiguration() {
        return encoderConfiguration;
    }
}