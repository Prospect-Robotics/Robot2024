package com.team2813.commands;

import com.team2813.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.Command;

public class PivotDownCommand extends Command{
    private final Intake intakeSubsystem;

    public PivotDownCommand(Intake intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
        addRequirements(intakeSubsystem);
    }
    @Override
    public void execute() {
        intakeSubsystem.pivotDown();
    }
}
