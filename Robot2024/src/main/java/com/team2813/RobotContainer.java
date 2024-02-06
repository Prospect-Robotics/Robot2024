// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team2813;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import frc.robot.Constants.OperatorConstants;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import com.team2813.commands.DefaultDriveCommand;
import com.team2813.commands.RunWholeOutake;
import com.team2813.commands.RunWholeIntake;
import com.team2813.subsystems.Drive;
import com.team2813.subsystems.Intake;
import static com.team2813.Constants.*;
import static com.team2813.Constants.DriverConstants.*;
import static com.team2813.Constants.OperatorConstants.*;

public class RobotContainer {
	private final SendableChooser<Command> autoChooser;
	private final Drive drive = new Drive();
	private final Intake intake = new Intake();

	private final XboxController driverController = new XboxController(driverControllerPort);
	public RobotContainer() {
		drive.setDefaultCommand(new DefaultDriveCommand(
                () -> -modifyAxis(driverController.getLeftY()) * Drive.MAX_VELOCITY,
                () -> -modifyAxis(driverController.getLeftX()) * Drive.MAX_VELOCITY,
                () -> -modifyAxis(driverController.getRightX()) * Drive.MAX_ANGULAR_VELOCITY,
                drive
        ));
		configureBindings();
		autoChooser = AutoBuilder.buildAutoChooser();
		SmartDashboard.putData("Auto", autoChooser);
	}

	public Drive getDrive() {
		return drive;
	}

	public Intake getIntake() {
		return intake;
	}

	private void configureBindings() {
		SLOWMODE_BUTTON.onTrue(new InstantCommand(() -> drive.enableSlowMode(true), drive));
		SLOWMODE_BUTTON.onFalse(new InstantCommand(() -> drive.enableSlowMode(false), drive));

		INTAKE_BUTTON.onTrue(new RunWholeIntake(intake));
		OUTTAKE_BUTTON.onTrue(new RunWholeOutake(intake));
	}

	public Command getAutonomousCommand() {
		return autoChooser.getSelected();
	}

	private static double deadband(double value, double deadband) {
        if (Math.abs(value) > deadband) {
            if (value > 0) {
                return (value - deadband) / (1 - deadband);
            } else {
                return (value + deadband) / (1 - deadband);
            }
        } else {
            return 0;
        }
    }

	private static double modifyAxis(double value) {
        value = deadband(value, 0.1);
        value = Math.copySign(value * value, value);
        return value;
    }
}
