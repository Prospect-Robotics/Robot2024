package com.team2813.commands;

import com.team2813.RobotSpecificConfigs;
import com.team2813.subsystems.Drive;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;

public class SaveSwerveOffsetsCommand extends Command {
	private final Drive drive;
	public SaveSwerveOffsetsCommand(Drive drive) {
		this.drive = drive;
		addRequirements(drive);
	}

	@Override
	public void initialize() {
		if (!DriverStation.isEnabled()) {
			RobotSpecificConfigs.saveSwerveConfig(
				RobotSpecificConfigs.swerveConfig().negate().add(drive.getOffsets()).negate()
			);
		}
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public boolean runsWhenDisabled() {
		return true;
	}
}
