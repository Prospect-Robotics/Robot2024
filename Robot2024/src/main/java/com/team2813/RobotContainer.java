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

import com.team2813.commands.DefaultDriveCommand;
import com.team2813.commands.DefaultShooterCommand;
import com.team2813.subsystems.Drive;
import com.team2813.subsystems.Shooter;
import com.team2813.subsystems.Magazine;
import com.team2813.subsystems.Amp;

import static com.team2813.Constants.*;
import static com.team2813.Constants.DriverConstants.*;
import static com.team2813.Constants.OperatorConstants.*;

public class RobotContainer {
	//robot subsystems and commands...

	private final SendableChooser<Command> autoChooser;
	private final Drive drive = new Drive();
	private final Shooter shooter = new Shooter();
	private final Magazine magazine = new Magazine();
	private final Amp amp = new Amp();

	//container for robot (subsystems, OI devices, commands)
	private final XboxController driverController = new XboxController(driverControllerPort);
	private final XboxController operatorController = new XboxController(operatorControllerPort);
	public RobotContainer() {
		drive.setDefaultCommand(new DefaultDriveCommand(
                () -> -modifyAxis(driverController.getLeftY()) * Drive.MAX_VELOCITY,
                () -> -modifyAxis(driverController.getLeftX()) * Drive.MAX_VELOCITY,
                () -> -modifyAxis(driverController.getRightX()) * Drive.MAX_ANGULAR_VELOCITY,
                drive
        ));
		shooter.setDefaultCommand(new DefaultShooterCommand(shooter, operatorController::getRightY));
		configureBindings();
		autoChooser = AutoBuilder.buildAutoChooser();
		SmartDashboard.putData("Auto", autoChooser);
	}

	public Drive getDrive() {
		return drive;
	}

	private void configureBindings() {
		slowmodeButton.onTrue(new InstantCommand(() -> drive.enableSlowMode(true), drive));
		slowmodeButton.onFalse(new InstantCommand(() -> drive.enableSlowMode(false), drive));
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
