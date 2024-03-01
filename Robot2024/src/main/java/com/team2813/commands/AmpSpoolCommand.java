package com.team2813.commands;

import com.team2813.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.Command;

public class AmpSpoolCommand extends Command {
	private double speed;
	private Shooter shooter;
	private static final double increase = 5;
	private static final double max = 100;
	public AmpSpoolCommand(Shooter shooter) {
		this.shooter = shooter;
		addRequirements(shooter);
	}
	@Override
	public void initialize() {
		//speed = increase;
		speed = 10;
	}
	@Override
	public void execute() {
		shooter.run(speed);
		//speed += increase;
		if (speed > max) {
			speed = max;
		}
	}
}