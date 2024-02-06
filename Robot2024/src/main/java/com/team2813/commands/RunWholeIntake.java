package com.team2813.commands;

import com.team2813.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class RunWholeIntake extends ParallelCommandGroup {

    public RunWholeIntake(Intake intakeSubsystem) {
        new ParallelCommandGroup(
            new IntakeCommand(intakeSubsystem),
            new PivotDownCommand(intakeSubsystem)
        );
    }
}