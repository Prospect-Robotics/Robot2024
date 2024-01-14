// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team2813;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import frc.robot.Constants.OperatorConstants;
import edu.wpi.first.wpilibj2.command.Command;
import com.team2813.subsystems.Drive;

public class RobotContainer {
	private final SendableChooser<Command> autoChooser;
	private final Drive drive = new Drive();
	public RobotContainer() {
		configureBindings();
		autoChooser = AutoBuilder.buildAutoChooser();
		SmartDashboard.putData("Auto", autoChooser);
	}

	public Drive getDrive() {
		return drive;
	}

	private void configureBindings() {}

	public Command getAutonomousCommand() {
		return autoChooser.getSelected();
	}
}
