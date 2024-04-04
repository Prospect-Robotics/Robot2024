package com.team2813.commands;

import com.team2813.RobotSpecificConfigs;
import com.team2813.lib2813.limelight.Limelight;
import com.team2813.subsystems.Drive;
import com.team2813.subsystems.Magazine;
import com.team2813.subsystems.Shooter;
import com.team2813.subsystems.ShooterPivot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

public class AutoAimCommand extends Command {
	// Math.PI - 1.330223 = angle from plate to top hard stop
	// other angle is from top plate of shooter to the output of shooter`
	private static final double top_rad = Math.PI - 1.330223 - 0.851438245792;

	private final Shooter shooter;
	private final ShooterPivot shooterPivot;
	private final Magazine mag;
	private final Drive drive;
	private final Limelight limelight;
	private boolean done;
	double magStart = 0;
	double shooterStart = 0;

	// speaker for red
	private static final Pose3d redSpeakerPos = new Pose3d(8.034371, 1.443727, 2.062736, new Rotation3d());
	private static final Pose3d blueSpeakerPos = new Pose3d(-8.034371, 1.443727, 2.062736, new Rotation3d());
	private Pose3d speakerPos;

	public AutoAimCommand(Shooter shooter, ShooterPivot shooterPivot, Magazine mag, Drive drive) {
		this.limelight = Limelight.getDefaultLimelight();
		this.shooter = shooter;
		this.drive = drive;
		this.shooterPivot = shooterPivot;
		this.mag = mag;
		addRequirements(shooter, drive, mag, shooterPivot);
	}

	public boolean isBlue() {
		return DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Blue;
	}

	private void useDistance(double distance) {
		shooterStart = Timer.getFPGATimestamp();
		distance *= 25;
		if (RobotSpecificConfigs.debug()) {
			SmartDashboard.putNumber("Auto-Aim Velocity", distance);
		}
		shooter.run(distance);
	}

	private void useRotationAngle(Rotation2d rotation) {
		if (DriverStation.isAutonomous()) {
			this.rotation = drive.getRotation();
		} else {
			this.rotation = rotation;
			drive.turnToFace(rotation);
		}
	}

	private void useShootingAngle(double angle) {
		double posRad = top_rad - angle;
		double posRotations = posRad / (Math.PI * 2);
		if (RobotSpecificConfigs.debug()) {
			SmartDashboard.putNumber("Auto-Aim Position (initial)", angle);
			SmartDashboard.putNumber("Auto-Aim Position (Rad)", posRad);
			SmartDashboard.putNumber("Auto-Aim Position (Rotations)", posRotations);
		}
		posRotations = MathUtil.clamp(
				posRotations,
				ShooterPivot.Position.TOP_HARD_STOP.getPos(),
				ShooterPivot.Position.BOTTOM_HARD_STOP.getPos());
		shooterPivot.setSetpoint(posRotations);
		shooterPivot.enable();
	}

	Rotation2d rotation;

	private Pose3d getPose() {
		// get value from limelight, use Drivetrain as backup option
		return limelight.getLocationalData().getBotpose().orElseGet(drive::get3DPose);
	}

	@Override
	public void initialize() {
		speakerPos = isBlue() ? blueSpeakerPos : redSpeakerPos;
		done = false;
		Pose3d pose = getPose();
		Transform3d diff = pose.minus(speakerPos);
		double z = Math.abs(diff.getZ()) - 0.266586;
		useRotationAngle(new Rotation2d(Math.atan2(diff.getY(), diff.getX())));
		double flatDistance = Math.hypot(diff.getX(), diff.getY());
		useDistance(Math.hypot(z, flatDistance));
		useShootingAngle(Math.atan2(z, flatDistance));
	}

	private boolean atRotation() {
		return DriverStation.isAutonomous() || Math.abs(drive.getTeleopPose().getRotation()
				.minus(rotation).getDegrees()) <= 3;
	}

	@Override
	public void execute() {
		boolean drivetrainGood = atRotation();
		boolean shooterGood = shooterPivot.atPosition();
		if (RobotSpecificConfigs.debug()) {
			SmartDashboard.putBoolean("drivetrain at position", drivetrainGood);
			SmartDashboard.putBoolean("shooter at position", shooterGood);
		}
		if (!done && drivetrainGood && shooterGood && Timer.getFPGATimestamp() - shooterStart >= 0.5) {
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
