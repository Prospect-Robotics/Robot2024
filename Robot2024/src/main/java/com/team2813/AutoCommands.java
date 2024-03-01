package com.team2813;

import com.team2813.commands.LockFunctionCommand;
import com.team2813.commands.ShootFromPosCommand;
import com.team2813.subsystems.Amp;
import com.team2813.subsystems.Intake;
import com.team2813.subsystems.IntakePivot;
import com.team2813.subsystems.Magazine;
import com.team2813.subsystems.Shooter;
import com.team2813.subsystems.Shooter.Angle;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutoCommands {
	private final Amp amp;
	private final Intake intake;
	private final IntakePivot intakePivot;
	private final Magazine magazine;
	private final Shooter shooter;

	public AutoCommands(Amp amp, Intake intake, IntakePivot intakePivot, Magazine magazine, Shooter shooter) {
		this.amp = amp;
		this.intake = intake;
		this.intakePivot = intakePivot;
		this.magazine = magazine;
		this.shooter = shooter;
	}

	private volatile Command startIntake = null;

	private Command createStartIntake() {
		return new SequentialCommandGroup(
			new LockFunctionCommand(intakePivot::positionReached, () -> intakePivot.setSetpoint(IntakePivot.Rotations.INTAKE_DOWN), intakePivot),
			new ParallelCommandGroup(
				new InstantCommand(intake::intake, intake), 
				new InstantCommand(magazine::runOnlyMag, magazine)
			)
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
			new LockFunctionCommand(intakePivot::positionReached, () -> intakePivot.setSetpoint(IntakePivot.Rotations.INTAKE_UP), intakePivot),
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
		return new ShootFromPosCommand(magazine, shooter, Angle.SUBWOOFER_FRONT, 20);
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
		return new ShootFromPosCommand(magazine, shooter, Angle.SUBWOOFER_SIDE, 20);
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
}
