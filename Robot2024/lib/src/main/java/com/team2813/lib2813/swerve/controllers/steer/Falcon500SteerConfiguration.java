package com.team2813.lib2813.swerve.controllers.steer;

import com.swervedrivespecialties.swervelib.ctre.CanCoderAbsoluteConfiguration;

public class Falcon500SteerConfiguration extends SteerConfiguration {

    private final String canbus;

    public Falcon500SteerConfiguration(int motorPort, String canbus, CanCoderAbsoluteConfiguration encoderConfiguration) {
        super(motorPort, encoderConfiguration);

        this.canbus = canbus;
    }

    public Falcon500SteerConfiguration(int motorPort, CanCoderAbsoluteConfiguration encoderConfiguration) {
        this(motorPort, "", encoderConfiguration);
    }

    public String getCanbus() {
        return canbus;
    }
}