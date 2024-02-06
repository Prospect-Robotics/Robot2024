package com.team2813.commands;

import com.team2813.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class RunWholeOutake extends ParallelCommandGroup {

    public RunWholeOutake(Intake intakeSubsystem) {
        new ParallelCommandGroup(
            new OutakeCommand(intakeSubsystem),
            new PivotUpCommand(intakeSubsystem)
        );
    }
}