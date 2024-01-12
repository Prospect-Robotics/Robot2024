package com.team2813.lib.swerve.controllers.drive;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;

public interface DriveController {
    public DriveController withPidConstants(double proportional, double integral, double derivative);
    public boolean hasPidConstants();
    public DriveController withFeedforward(SimpleMotorFeedforward feedforward);
    public boolean hasFeedForward();

    public double getDistanceDriven();

    public void setReferenceVelocity(double velocity);
    public double getStateVelocity();

    public void resetEncoder();

    default void addDashboardEntries(ShuffleboardContainer container) {
        container.addNumber("Current Velocity", this::getStateVelocity);
    }
}