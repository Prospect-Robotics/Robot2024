// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team2813;

import static com.team2813.Constants.DriverConstants.ampIntakeButton;
import static com.team2813.Constants.DriverConstants.autoAimButton;
import static com.team2813.Constants.DriverConstants.driverControllerPort;
import static com.team2813.Constants.DriverConstants.orientButton;
import static com.team2813.Constants.DriverConstants.slowmodeButton;
import static com.team2813.Constants.DriverConstants.spoolAutoAimButton;
import static com.team2813.Constants.OperatorConstants.altOuttakeButton;
import static com.team2813.Constants.OperatorConstants.ampInButton;
import static com.team2813.Constants.OperatorConstants.ampOutButton;
import static com.team2813.Constants.OperatorConstants.autoShootButton;
import static com.team2813.Constants.OperatorConstants.climbDownButton;
import static com.team2813.Constants.OperatorConstants.climbUpButton;
import static com.team2813.Constants.OperatorConstants.farSpeaker;
import static com.team2813.Constants.OperatorConstants.intakeButton;
import static com.team2813.Constants.OperatorConstants.operatorControllerPort;
import static com.team2813.Constants.OperatorConstants.outtakeButton;
import static com.team2813.Constants.OperatorConstants.herdButton;
import static com.team2813.Constants.OperatorConstants.shootButton;
import static com.team2813.Constants.OperatorConstants.shootPodium;
import static com.team2813.Constants.OperatorConstants.shootWooferFront;
import static com.team2813.Constants.OperatorConstants.shootWooferSide;
import static com.team2813.Constants.OperatorConstants.sourceIntake;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.team2813.commands.AutoAimCommand;
import com.team2813.commands.AutonomousAutoAimCommand;
import com.team2813.commands.DefaultDriveCommand;
import com.team2813.commands.DefaultShooterCommand;
import com.team2813.commands.LockFunctionCommand;
import com.team2813.commands.SaveSwerveOffsetsCommand;
import com.team2813.commands.ShootFromPosCommand;
import com.team2813.subsystems.Amp;
import com.team2813.subsystems.Climber;
import com.team2813.subsystems.Drive;
import com.team2813.subsystems.Intake;
import com.team2813.subsystems.IntakePivot;
import com.team2813.subsystems.IntakePivot.Rotations;
import com.team2813.subsystems.LEDs;
import com.team2813.subsystems.Magazine;
import com.team2813.subsystems.Shooter;
import com.team2813.subsystems.ShooterPivot;
import com.team2813.subsystems.ShooterPivot.Position;

import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class RobotContainer {
	private final SendableChooser<Command> autoChooser;

	private final Drive drive = new Drive();
	private final Shooter shooter = new Shooter();
	private final Amp amp = new Amp();
	private final Intake intake = new Intake();
	private final Magazine mag = new Magazine();
	private final IntakePivot intakePivot = new IntakePivot();
	private final ShooterPivot shooterPivot = new ShooterPivot();
	// needs to not be garbage collected; all work in periodic method of subsystem, and no buttons needed
	@SuppressWarnings("unused")
	private final LEDs leds = new LEDs(mag);
	private final Climber climber = new Climber();

	private final XboxController driverController = new XboxController(driverControllerPort);
	private final XboxController operatorController = new XboxController(operatorControllerPort);

	public RobotContainer() {
		drive.setDefaultCommand(new DefaultDriveCommand(
				() -> -modifyAxis(driverController.getLeftY()) * Drive.MAX_VELOCITY,
				() -> -modifyAxis(driverController.getLeftX()) * Drive.MAX_VELOCITY,
				() -> -modifyAxis(driverController.getRightX()) * Drive.MAX_ANGULAR_VELOCITY,
				drive));
		shooterPivot.setDefaultCommand(new DefaultShooterCommand(shooterPivot, operatorController::getRightY));
		AutoCommands autoCommands = new AutoCommands(intake, intakePivot, mag, shooter, shooterPivot);
		configureBindings(autoCommands);
		addAutoCommands(autoCommands);
		autoChooser = AutoBuilder.buildAutoChooser();
		SmartDashboard.putData("Auto", autoChooser);
		Shuffleboard.getTab("swerve").add(new SaveSwerveOffsetsCommand(drive));
		Shuffleboard.getTab("swerve").addBoolean("offsets loaded", RobotSpecificConfigs::loadedSwerveConfig);
		Shuffleboard.getTab("swerve").addBoolean("debug", RobotSpecificConfigs::debug);
	}

	private void addAutoCommands(AutoCommands autoCommands) {
		NamedCommands.registerCommand("start-intake", autoCommands.startIntake());
		NamedCommands.registerCommand("stop-intake", autoCommands.stopIntake());
		NamedCommands.registerCommand("shoot-front", autoCommands.shootFront());
		NamedCommands.registerCommand("shoot-side", autoCommands.shootSide());
		NamedCommands.registerCommand("shoot-auto", new AutonomousAutoAimCommand(shooter, shooterPivot, mag, drive::get3DPose));
		NamedCommands.registerCommand("shoot-amp", autoCommands.shootAmp());
	}

	private void configureBindings(AutoCommands autoCommands) {
		slowmodeButton.whileTrue(new InstantCommand(() -> drive.enableSlowMode(true), drive));
		slowmodeButton.onFalse(new InstantCommand(() -> drive.enableSlowMode(false), drive));

		for (int i = 5800; i <= 5807; i++) {
			PortForwarder.add(i, "limelight.local", i);
		}

		// intake & outtake buttons
		intakeButton.onTrue(autoCommands.startIntake());
		intakeButton.onFalse(autoCommands.stopIntake());

		outtakeButton.onTrue(new ParallelCommandGroup(
				new InstantCommand(intake::outtakeNote, intake),
				new InstantCommand(mag::reverseMag, mag)));

		altOuttakeButton.onTrue(new SequentialCommandGroup(
				new InstantCommand(() -> intakePivot.setSetpoint(Rotations.INTAKE_DOWN), intakePivot),
				new WaitCommand(0.2),
				new ParallelCommandGroup(
						new InstantCommand(intake::outtakeNote, intake),
						new InstantCommand(mag::reverseMag, mag))));
		altOuttakeButton.onFalse(autoCommands.stopIntake());

		outtakeButton.onFalse(autoCommands.stopIntake());

		ampIntakeButton.onTrue(autoCommands.shootAmp());

		/*
		 * ampIntakeButton.onFalse(
		 * new InstantCommand(amp::stop, amp)
		 * //new InstantCommand(amp::ampStop, amp)
		 * );
		 * ampOuttakeButton.onTrue(
		 * new InstantCommand(amp::pushNoteOut, amp)
		 * //new InstantCommand(amp::ampOuttake, amp)
		 * );
		 * ampOuttakeButton.onFalse(
		 * new InstantCommand(amp::stop, amp)
		 * //new InstantCommand(amp::ampStop, amp)
		 * );
		 */

		climbUpButton.onTrue(
				new InstantCommand(climber::extend, climber));

		climbUpButton.onFalse(
				new InstantCommand(climber::stop, climber));

		climbDownButton.onTrue(
				new InstantCommand(climber::retract, climber));

		climbDownButton.onFalse(
				new InstantCommand(climber::stop, climber));

		ampInButton.onTrue(
				new InstantCommand(amp::ampIntake, amp));
		ampInButton.onFalse(
				new InstantCommand(amp::ampStop, amp));
		ampOutButton.onTrue(
				new InstantCommand(amp::ampOuttake, amp));
		ampOutButton.onFalse(
				new InstantCommand(amp::ampStop, amp));

		orientButton.onTrue(
				new InstantCommand(drive::orientForward, drive));

		shootButton.onTrue(new SequentialCommandGroup(
			new ParallelRaceGroup(
				new WaitCommand(1),
				new LockFunctionCommand(shooter::atVelocity, () -> shooter.run(75), shooter)
			),
			new InstantCommand(mag::runMagKicker, mag),
			new WaitCommand(0.5),
			new ParallelCommandGroup(
				new InstantCommand(mag::stop, mag),
				new InstantCommand(shooter::stop, shooter)
			)
		));

		spoolAutoAimButton.onTrue(
				new AutonomousAutoAimCommand(shooter, shooterPivot, mag, drive::get3DPose)
		// new LockFunctionCommand(shooterPivot::atPosition, () ->
		// shooterPivot.setSetpoint(Position.TEST), shooterPivot, drive)
		);

		autoShootButton.onTrue(
			new SequentialCommandGroup(
				new ParallelRaceGroup(
					new WaitCommand(0.5),
					new LockFunctionCommand(shooter::atVelocity, () -> shooter.run(90), shooter)
				),
				new InstantCommand(mag::runMagKicker, mag),
				new ParallelCommandGroup(
					new InstantCommand(mag::stop, mag),
					new InstantCommand(shooter::stop, shooter)
				)
			)
		);

		shootWooferFront.onTrue(autoCommands.shootFront());
		shootWooferSide.onTrue(autoCommands.shootSide());
		herdButton.onTrue(new ShootFromPosCommand(mag, shooter, shooterPivot, Position.HERD, 65));
		shootPodium.onTrue(autoCommands.shootPodium());
		farSpeaker.onTrue(autoCommands.farSpeaker());
		autoAimButton.onTrue(new AutoAimCommand(shooter, shooterPivot, mag, drive));

		sourceIntake.onTrue(
			new SequentialCommandGroup(
				new LockFunctionCommand(shooterPivot::atPosition, () -> shooterPivot.setSetpoint(Position.SOURCE_INTAKE), shooterPivot),
				new InstantCommand(shooter::reverse, shooter)
			)
		);
		sourceIntake.onFalse(
			new SequentialCommandGroup(
				new ParallelCommandGroup(
					new InstantCommand(mag::reverseMag, mag),
					new InstantCommand(intake::outtakeNote, intake)
				),
				new WaitCommand(0.1),
				new ParallelCommandGroup(
					new InstantCommand(shooter::stop, shooter),
					new InstantCommand(intake::intake, intake),
					new InstantCommand(mag::runOnlyMag, mag)
				),
				new WaitCommand(0.1),
				new ParallelCommandGroup(
					new InstantCommand(intake::stopIntakeMotor, intake),
					new InstantCommand(mag::stop, mag)
				)
			)
		);
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
