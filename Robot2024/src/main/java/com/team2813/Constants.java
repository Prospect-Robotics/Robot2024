// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team2813;

import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
	public static class OperatorConstants {
		public static final int operatorControllerPort = 1;
		public static final CommandPS4Controller operatorController = new CommandPS4Controller(operatorControllerPort);

		//operator controls
		public static final Trigger intakeButton = operatorController.R1();
		public static final Trigger outtakeButton = operatorController.L1();
		public static final Trigger shootButton = operatorController.square();
		public static final Trigger spoolAutoAimButton = operatorController.triangle();
		public static final Trigger spoolPodiumButton = operatorController.circle();
		public static final Trigger spoolAmpButton = operatorController.cross();
		public static final Trigger climbButton = operatorController.share();
		public static final Trigger climbManualUpButton = operatorController.povUp();
		public static final Trigger climbManualDownButton = operatorController.povDown();

	}
	public static class DriverConstants {
		public static final int driverControllerPort = 0;
		public static final CommandPS4Controller driverController = new CommandPS4Controller(driverControllerPort);

		//driver controls
		public static final Trigger slowmodeButton = driverController.L1();
		public static final Trigger resetSwerve = driverController.PS();
	}

	// front right swerve module
	public static final int FRONT_RIGHT_DRIVE_ID = 1;
	public static final int FRONT_RIGHT_ENCODER_ID = 2;
	public static final int FRONT_RIGHT_STEER_ID = 3;

	// front left swerve module
	public static final int FRONT_LEFT_DRIVE_ID = 4;
	public static final int FRONT_LEFT_ENCODER_ID = 5;
	public static final int FRONT_LEFT_STEER_ID = 6;

	// back right swerve module
	public static final int BACK_RIGHT_DRIVE_ID = 7;
	public static final int BACK_RIGHT_ENCODER_ID = 8;
	public static final int BACK_RIGHT_STEER_ID = 9;

	// back left swerve module
	public static final int BACK_LEFT_DRIVE_ID = 10;
	public static final int BACK_LEFT_ENCODER_ID = 11;
	public static final int BACK_LEFT_STEER_ID = 12;

	// pigeon
	public static final int PIGEON_ID = 13;

	//Mechanism CAN IDs
	public static final int INTAKE = 14;
	public static final int KICKER = 15;
	public static final int SHOOTER_1 = 16;
	public static final int SHOOTER_2 = 17;
	public static final int SHOOTER_PIVOT= 18;
	public static final int SHOOTER_ENCODER = 19;
	public static final int CLIMBER = 20;
	public static final int MAGAZINE = 21;
	public static final int INTAKE_ENCODER = 22;
	public static final int INTAKE_PIVOT = 23;
	public static final int AMP = 24;
}
