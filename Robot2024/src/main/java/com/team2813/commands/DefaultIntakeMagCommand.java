package com.team2813.commands;

import com.team2813.subsystems.Intake;
import com.team2813.subsystems.Magazine;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class DefaultIntakeMagCommand extends SequentialCommandGroup {

    public DefaultIntakeMagCommand(Intake intakeSubsystem, Magazine magazineSubsystem) {
        super(
                new InstantCommand(intakeSubsystem::intake, intakeSubsystem),
                new WaitCommand(0.25),
                new InstantCommand(intakeSubsystem::stopIntakeMotor, intakeSubsystem),
                new InstantCommand(magazineSubsystem::runMagKicker, magazineSubsystem),
                new WaitCommand(0.25),
                new InstantCommand(magazineSubsystem::stopMagKicker, magazineSubsystem)

        );
        addRequirements(intakeSubsystem);
        addRequirements(magazineSubsystem);
    }
}
