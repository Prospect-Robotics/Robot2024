package com.team2813.lib2813.swerve.controllers.steer;

import com.swervedrivespecialties.swervelib.ctre.CanCoderAbsoluteConfiguration;
import com.team2813.lib2813.util.Port;

public class Falcon500SteerConfiguration extends SteerConfiguration {

    private final String canbus;

    public Falcon500SteerConfiguration(Port motorPort, String canbus, CanCoderAbsoluteConfiguration encoderConfiguration) {
        super(motorPort, encoderConfiguration);

        this.canbus = canbus;
    }

    public Falcon500SteerConfiguration(Port motorPort, CanCoderAbsoluteConfiguration encoderConfiguration) {
        this(motorPort, "", encoderConfiguration);
    }

    public String getCanbus() {
        return canbus;
    }
}