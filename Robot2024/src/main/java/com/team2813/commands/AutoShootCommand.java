package com.team2813.commands;

import com.team2813.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoShootCommand extends SequentialCommandGroup {

    public AutoShootCommand(Shooter shootSubsystem) {
        super(
                new InstantCommand(shootSubsystem::runAuto, shootSubsystem),
                new WaitCommand(5),
                new InstantCommand(shootSubsystem::stop, shootSubsystem)
        );
    }
}
