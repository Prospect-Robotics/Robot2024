package com.team2813.commands;

import com.team2813.subsystems.Intake;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;

public class IntakeCommand extends Command{
    private final Intake intakeSubsystem;

    public IntakeCommand(Intake intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
        addRequirements(intakeSubsystem);
    }
    @Override
    public void execute() {
        intakeSubsystem.intake();
    }

    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.stopIntakeMotor();
        intakeSubsystem.zeroSensors();
    }
}
