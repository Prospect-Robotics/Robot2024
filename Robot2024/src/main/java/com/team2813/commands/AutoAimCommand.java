package com.team2813.commands;

import com.team2813.subsystems.Drive;
import com.team2813.subsystems.Magazine;
import com.team2813.subsystems.Shooter;
import com.team2813.Robot;
import com.team2813.lib2813.limelight.Limelight;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;

public class AutoAimCommand extends Command {
  private final Shooter shooter;
  private final Magazine mag;
  private final Drive drive;
  private final Limelight limelight;
  private boolean done;
  long magStart = 0;

  private Pose3d speakerPos = new Pose3d(7.846862, -1.455030, 2.364370, new Rotation3d());

  public AutoAimCommand(Shooter shooter, Magazine mag, Drive drive, Limelight limelight) {
	this.limelight = limelight;
    this.shooter = shooter;
	this.drive = drive;
	this.mag = mag;
	addRequirements(shooter, drive);
  }

  private void useDistance(double distance) {
	shooter.run(distance * 10);
  }

  private void useRotationAngle(double angle) {
	drive.turnToFace(new Rotation2d(angle));
  }

  private void useShootingAngle(double angle) {
	shooter.setSetpoint(Math.PI * 2 / angle);
	shooter.enable();
  }

  private Pose3d getPose() {
	// get value from limelight, use Drivetrain as backup option
	return limelight.getLocationalData().getBotpose().orElseGet(drive::get3DPose);
  }

  @Override
  public void initialize() {
	done = false;
	Pose3d pose = getPose();
	double diffX = speakerPos.getX() - pose.getX();
	double diffY = speakerPos.getY() - pose.getY();
	double diffZ = speakerPos.getZ() - pose.getZ();
	useRotationAngle(Math.atan2(diffY, diffX));
	double flatDistance = Math.hypot(diffX, diffY);
	useDistance(Math.hypot(diffZ, flatDistance));
	useShootingAngle(Math.atan2(diffZ, flatDistance));
  }

  private boolean atRotation() {
	return Math.abs(getPose().getRotation().toRotation2d()
		.minus(drive.getRotation()).getDegrees()) < 2;
  }

  @Override
  public void execute() {
	if (shooter.atPosition() && atRotation()) {
		mag.runMagKicker();
		done = true;
		magStart = System.currentTimeMillis();
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
	return done && System.currentTimeMillis() - magStart > 1_000;
  }
}
