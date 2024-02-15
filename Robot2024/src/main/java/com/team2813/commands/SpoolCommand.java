package com.team2813.commands;

import com.team2813.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.Command;

public class SpoolCommand extends Command {
	private double speed;
	private Shooter shooter;
	private static final double increase = 0.5;
	private static final double max = 1;
	public SpoolCommand(Shooter shooter) {
		this.shooter = shooter;
		addRequirements(shooter);
	}
	@Override
	public void initialize() {
		speed = increase;
	}
	@Override
	public void execute() {
		shooter.run(speed);
		if (speed < max) {
			speed += increase;
		}
	}
	@Override
	public boolean isFinished() {
		return speed >= max;
	}
}
