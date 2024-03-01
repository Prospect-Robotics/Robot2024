package com.team2813.commands;

import com.team2813.subsystems.Magazine;
import com.team2813.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ShootFromPosCommand extends SequentialCommandGroup {
	public ShootFromPosCommand(Magazine magazine, Shooter shooter, Shooter.Angle pos, double shooterSpeed) {
		super(
			// new LockFunctionCommand(shooter::atSetpoint, () -> shooter.setSetpoint(pos), shooter),
			new InstantCommand(() -> shooter.run(shooterSpeed), shooter),
			new WaitCommand(0.5),
			new InstantCommand(magazine::runMagKicker, magazine),
			new WaitCommand(0.5),
			new ParallelCommandGroup(
				new InstantCommand(magazine::stop, magazine),
				new InstantCommand(shooter::stop, shooter)
			)
		);
	}
}
