package com.team2813.commands;

import com.team2813.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoIntakeCommand extends SequentialCommandGroup {

    public AutoIntakeCommand(Intake intakeSubsystem) {
        super(
                new InstantCommand(intakeSubsystem::intakeNote, intakeSubsystem),
                new WaitCommand(0.25),
                new InstantCommand(intakeSubsystem::stopIntakeMotor, intakeSubsystem)
        );
    }
}
