package com.team2813.commands;

import com.team2813.subsystems.Magazine;
import com.team2813.subsystems.Shooter;
import com.team2813.subsystems.ShooterPivot;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ShootFromPosCommand extends SequentialCommandGroup {
	public ShootFromPosCommand(Magazine magazine, Shooter shooter, ShooterPivot pivot, ShooterPivot.Position pos,
			double shooterSpeed) {
		super(
				new ParallelRaceGroup(
					new WaitCommand(0.5),
					new ParallelCommandGroup(
						new LockFunctionCommand(pivot::atPosition, () -> pivot.setSetpoint(pos), pivot),
						new LockFunctionCommand(shooter::atVelocity, () -> shooter.run(shooterSpeed), shooter)
					)
				),
				new InstantCommand(magazine::runMagKicker, magazine),
				new WaitCommand(0.5),
				new ParallelCommandGroup(
					new InstantCommand(magazine::stop, magazine),
					new InstantCommand(shooter::stop, shooter)
				)
		);
	}
}
