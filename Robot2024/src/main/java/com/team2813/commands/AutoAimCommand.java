package com.team2813.commands;

import com.team2813.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;

public class AutoAimCommand extends Command {
  private final Shooter shooter;
  public AutoAimCommand(Shooter shooter) {
    this.shooter = shooter;
  }

  @Override
  public void execute() {
	// do stuff
  }
}
