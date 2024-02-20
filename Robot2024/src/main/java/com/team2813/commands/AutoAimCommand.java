package com.team2813.commands;

import com.team2813.subsystems.Drive;
import com.team2813.subsystems.Shooter;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj2.command.Command;

public class AutoAimCommand extends Command {
  private final Shooter shooter;
  private final Drive drive;

  private Pose3d speakerPos = new Pose3d(7.846862, -1.455030, 2.364370, new Rotation3d());

  public AutoAimCommand(Shooter shooter, Drive drive) {
    this.shooter = shooter;
	this.drive = drive;
	addRequirements(shooter, drive);
  }

  private void useDistance(double distance) {
	shooter.run(distance * 10);
  }

  private void useRotationAngle(double angle) {
	drive.turnToFace(new Rotation2d(angle));
  }

  private void useShootingAngle(double angle) {
	shooter.setSetpoint(angle);
	shooter.enable();
  }

  private Pose3d getPose() {
	return null; //TODO: get position from limelight
  }

  @Override
  public void initialize() {
	Pose3d pose = getPose();
	double diffX = speakerPos.getX() - pose.getX();
	double diffY = speakerPos.getY() - pose.getY();
	double diffZ = speakerPos.getZ() - pose.getZ();
	useRotationAngle(Math.toDegrees(Math.atan2(diffY, diffX)));
	double flatDistance = Math.hypot(diffX, diffY);
	useDistance(Math.hypot(diffZ, flatDistance));
	useShootingAngle(Math.toDegrees(Math.atan2(diffZ, flatDistance)));
  }

  @Override
  public boolean isFinished() {
	return true;
  }
}
