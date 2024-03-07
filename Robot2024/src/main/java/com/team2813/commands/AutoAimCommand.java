package com.team2813.commands;

import com.team2813.lib2813.limelight.Limelight;
import com.team2813.subsystems.Drive;
import com.team2813.subsystems.Magazine;
import com.team2813.subsystems.Shooter;
import com.team2813.subsystems.ShooterPivot;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

public class AutoAimCommand extends Command {
  private final Shooter shooter;
  private final ShooterPivot shooterPivot;
  private final Magazine mag;
  private final Drive drive;
  private final Limelight limelight;
  private boolean done;
  double magStart = 0;

  // speaker for red
  private static final Pose3d redSpeakerPos = new Pose3d(7.846862, 1.455030, 2.364370, new Rotation3d());
  private static final Pose3d blueSpeakerPos = new Pose3d(-7.846862, 1.455030, 2.364370, new Rotation3d());
  private Pose3d speakerPos;

  public AutoAimCommand(Shooter shooter, ShooterPivot shooterPivot, Magazine mag, Drive drive) {
	this.limelight = Limelight.getDefaultLimelight();
    this.shooter = shooter;
	this.drive = drive;
	this.shooterPivot = shooterPivot;
	this.mag = mag;
	addRequirements(shooter, drive, mag);
  }

  public boolean isBlue() {
	return DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Blue;
  }

  private void useDistance(double distance) {
	shooter.run(distance * 10);
  }

  private void useRotationAngle(double angle) {
	drive.turnToFace(new Rotation2d(angle));
  }

  private void useShootingAngle(double angle) {
	shooterPivot.setSetpoint(Math.PI * 2 / angle);
	shooterPivot.enable();
  }

  private Pose3d getPose() {
	// get value from limelight, use Drivetrain as backup option
	return limelight.getLocationalData().getBotpose().orElseGet(drive::get3DPose);
  }

  @Override
  public void initialize() {
	speakerPos = isBlue() ? blueSpeakerPos : redSpeakerPos;
	done = false;
	Pose3d pose = getPose();
	Transform3d diff = speakerPos.minus(pose);
	useRotationAngle(Math.atan2(diff.getY(), diff.getX()));
	double flatDistance = Math.hypot(diff.getX(), diff.getY());
	useDistance(Math.hypot(diff.getZ(), flatDistance));
	useShootingAngle(Math.atan2(diff.getZ(), flatDistance));
  }

  private boolean atRotation() {
	return Math.abs(getPose().getRotation().toRotation2d()
		.minus(drive.getRotation()).getDegrees()) < 2;
  }

  @Override
  public void execute() {
	if (shooterPivot.atPosition() && atRotation()) {
		mag.runMagKicker();
		done = true;
		magStart = Timer.getFPGATimestamp();
	}
  }

  @Override
  public void end(boolean interrupted) {
	drive.stop();
	shooter.stop();
	mag.stop();
  }

  @Override
  public boolean isFinished() {
	return done && Timer.getFPGATimestamp() - magStart >= 1;
  }
}
