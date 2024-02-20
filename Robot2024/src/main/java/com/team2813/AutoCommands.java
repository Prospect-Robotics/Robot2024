package com.team2813;

import com.team2813.commands.ShootFromPosCommand;
import com.team2813.subsystems.Intake;
import com.team2813.subsystems.IntakePivot;
import com.team2813.subsystems.IntakePivot.Rotations;
import com.team2813.subsystems.Magazine;
import com.team2813.subsystems.Shooter;
import com.team2813.subsystems.ShooterPivot;
import com.team2813.subsystems.ShooterPivot.Position;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoCommands {
	private final Intake intake;
	private final IntakePivot intakePivot;
	private final Magazine magazine;
	private final Shooter shooter;
	private final ShooterPivot shooterPivot;

	public AutoCommands(Intake intake, IntakePivot intakePivot, Magazine magazine, Shooter shooter, ShooterPivot shooterPivot) {
		this.intake = intake;
		this.intakePivot = intakePivot;
		this.magazine = magazine;
		this.shooter = shooter;
		this.shooterPivot = shooterPivot;
	}

	private volatile Command startIntake = null;

	private Command createStartIntake() {
		return new ParallelCommandGroup(
			new InstantCommand(() -> intakePivot.setSetpoint(Rotations.INTAKE_DOWN), intakePivot),
			new InstantCommand(intake::intake, intake),
			new InstantCommand(magazine::runOnlyMag, magazine)
		);
	}

	public Command startIntake() {
		if (startIntake == null) {
			synchronized (this) {
				if (startIntake == null) {
					startIntake = createStartIntake();
				}
			}
		}
		return startIntake;
	}

	private volatile Command stopIntake = null;

	private Command createStopIntake() {
		return new ParallelCommandGroup(
			new InstantCommand(() -> intakePivot.setSetpoint(Rotations.INTAKE_UP), intakePivot),
			new InstantCommand(intake::stopIntakeMotor, intake),
			new InstantCommand(magazine::stop, magazine)
		);
	}

	public Command stopIntake() {
		if (stopIntake == null) {
			synchronized (this) {
				if (stopIntake == null) {
					stopIntake = createStopIntake();
				}
			}
		}
		return stopIntake;
	}

	private volatile Command shootFront = null;

	private Command createShootFront() {
		// TODO: get correct angle and speed
		return new ShootFromPosCommand(magazine, shooter, shooterPivot, Position.SUBWOOFER_FRONT, 26.5);
	}

	public Command shootFront() {
		if (shootFront == null) {
			synchronized (this) {
				if (shootFront == null) {
					shootFront = createShootFront();
				}
			}
		}
		return shootFront;
	}

	private volatile Command shootSide = null;

	private Command createShootSide() {
		// TODO: get correct angle and speed
		return new ShootFromPosCommand(magazine, shooter, shooterPivot, Position.SUBWOOFER_SIDE, 25);
	}

	public Command shootSide() {
		if (shootSide == null) {
			synchronized (this) {
				if (shootSide == null) {
					shootSide = createShootSide();
				}
			}
		}
		return shootSide;
	}

	private volatile Command shootAuto = null;

	private Command createShootAuto() {
		// TODO: replace with auto aim when completed
		return Commands.none();
	}

	public Command shootAuto() {
		if (shootAuto == null) {
			synchronized (this) {
				if (shootSide == null) {
					shootSide = createShootAuto();
				}
			}
		}
		return shootSide;
	}

	private volatile Command shootAmp = null;

	private Command createShootAmp() {
		return new SequentialCommandGroup(
			new InstantCommand(() -> shooter.run(10), shooter),
			new WaitCommand(0.2),
			new InstantCommand(magazine::runMagKicker, magazine),
			new WaitCommand(1),
			new ParallelCommandGroup(
				new InstantCommand(shooter::stop, shooter),
				new InstantCommand(magazine::stop, magazine)
			)
		);
	}

	public Command shootAmp() {
		if (shootAmp == null) {
			synchronized (this) {
				if (shootAmp == null) {
					shootAmp = createShootAmp();
				}
			}
		}
		return shootAmp;
	}
}