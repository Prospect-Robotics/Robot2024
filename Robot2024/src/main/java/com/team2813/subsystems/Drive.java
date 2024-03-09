package com.team2813.subsystems;

import static com.team2813.Constants.BACK_LEFT_DRIVE_ID;
import static com.team2813.Constants.BACK_LEFT_ENCODER_ID;
import static com.team2813.Constants.BACK_LEFT_STEER_ID;
import static com.team2813.Constants.BACK_RIGHT_DRIVE_ID;
import static com.team2813.Constants.BACK_RIGHT_ENCODER_ID;
import static com.team2813.Constants.BACK_RIGHT_STEER_ID;
import static com.team2813.Constants.FRONT_LEFT_DRIVE_ID;
import static com.team2813.Constants.FRONT_LEFT_ENCODER_ID;
import static com.team2813.Constants.FRONT_LEFT_STEER_ID;
import static com.team2813.Constants.FRONT_RIGHT_DRIVE_ID;
import static com.team2813.Constants.FRONT_RIGHT_ENCODER_ID;
import static com.team2813.Constants.FRONT_RIGHT_STEER_ID;
import static com.team2813.Constants.PIGEON_ID;

import java.util.OptionalLong;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.ClosedLoopOutputType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModuleConstantsFactory;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import com.ctre.phoenix6.mechanisms.swerve.utility.PhoenixPIDController;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
import com.swervedrivespecialties.swervelib.SdsModuleConfigurations;
import com.team2813.RobotSpecificConfigs;
import com.team2813.RobotSpecificConfigs.SwerveConfig;
import com.team2813.lib2813.limelight.Limelight;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drive extends SubsystemBase {
	private static class PublicisizedKinematics extends SwerveDrivetrain {
		public PublicisizedKinematics(SwerveDrivetrainConstants driveTrainConstants, SwerveModuleConstants... modules) {
			super(driveTrainConstants, modules);
		}

		public ChassisSpeeds getChassisSpeeds() {
			return m_kinematics.toChassisSpeeds(getState().ModuleStates);
		}
	}

    public static final double MAX_VELOCITY = 6380.0 / 60.0 *
            SdsModuleConfigurations.MK4I_L2.getDriveReduction() *
            SdsModuleConfigurations.MK4I_L2.getWheelDiameter() * Math.PI; // m/s
    public static final double MAX_ANGULAR_VELOCITY = Math.PI * 8; // radians per second

    private double multiplier = 1;

	private final Limelight limelight;

	SwerveDrivetrain drivetrain;

    public Drive() {
		SwerveConfig offsets = RobotSpecificConfigs.swerveConfig();
		limelight = Limelight.getDefaultLimelight();
		// rotations
        double frontLeftSteerOffset = offsets.frontLeftOffset(); //0.210693
        double frontRightSteerOffset = offsets.frontRightOffset(); //-0.408936
        double backLeftSteerOffset = offsets.backLeftOffset(); //0.372803
        double backRightSteerOffset = offsets.backRightOffset(); //-0.214111

		// tune
		Slot0Configs steerGains = new Slot0Configs()
			.withKP(50).withKI(0).withKD(0.2)
			.withKS(0).withKV(1.5).withKA(0);
		// tune
		Slot0Configs driveGains = new Slot0Configs()
			.withKP(3).withKI(0).withKD(0)
			.withKS(0).withKV(0).withKA(0);

		SwerveDrivetrainConstants drivetrainConstants = new SwerveDrivetrainConstants()
			.withPigeon2Id(PIGEON_ID)
			.withCANbusName(RobotSpecificConfigs.swerveCanbus());
		SwerveModuleConstantsFactory constantCreator = new SwerveModuleConstantsFactory()
			.withDriveMotorGearRatio(6.75)
			.withSteerMotorGearRatio(150.0 / 7)
			.withWheelRadius(1.75)
			.withSlipCurrent(300) // tune :)
			.withSteerMotorGains(steerGains)
			.withDriveMotorGains(driveGains)
			.withSteerMotorClosedLoopOutput(ClosedLoopOutputType.Voltage)
			.withDriveMotorClosedLoopOutput(RobotSpecificConfigs.driveOutput())
			.withSpeedAt12VoltsMps(MAX_VELOCITY)
			.withFeedbackSource(RobotSpecificConfigs.swerveFeedback())
			.withCouplingGearRatio(3.5) // tune :P
			.withSteerMotorInverted(true);
		double frontDist = 0.381; // x
		double leftDist = 0.3302; // y
		SwerveModuleConstants frontLeft = constantCreator.createModuleConstants(
			FRONT_LEFT_STEER_ID, FRONT_LEFT_DRIVE_ID,
			FRONT_LEFT_ENCODER_ID, frontLeftSteerOffset,
			frontDist, leftDist,
			true
		);
		SwerveModuleConstants frontRight = constantCreator.createModuleConstants(
			FRONT_RIGHT_STEER_ID, FRONT_RIGHT_DRIVE_ID,
			FRONT_RIGHT_ENCODER_ID, frontRightSteerOffset,
			frontDist, -leftDist,
			true
		);
		SwerveModuleConstants backLeft = constantCreator.createModuleConstants(
			BACK_LEFT_STEER_ID, BACK_LEFT_DRIVE_ID,
			BACK_LEFT_ENCODER_ID, backLeftSteerOffset,
			-frontDist, leftDist,
			true
		);
		SwerveModuleConstants backRight = constantCreator.createModuleConstants(
			BACK_RIGHT_STEER_ID, BACK_RIGHT_DRIVE_ID,
			BACK_RIGHT_ENCODER_ID, backRightSteerOffset,
			-frontDist, -leftDist,
			true
		);
		SwerveModuleConstants[] constants = new SwerveModuleConstants[]{frontLeft, frontRight, backLeft, backRight};
		PublicisizedKinematics drivetrain = new PublicisizedKinematics(drivetrainConstants, constants);
		this.drivetrain = drivetrain;
		for (int i = 0; i < 4; i++) {
			setLimits(i);
		}
		AutoBuilder.configureHolonomic(
			this::getPose,
			this::resetOdometry,
			drivetrain::getChassisSpeeds,
			this::drive,
			new HolonomicPathFollowerConfig(
				new PIDConstants(0.4, 0, 0), // Translation PID
				new PIDConstants(0, 0, 0), // Rotation PID
				MAX_VELOCITY,
				0.410178,
				new ReplanningConfig()
				),
			Drive::onRed,
			this);
		//
		ShuffleboardTab tab = Shuffleboard.getTab("swerve");
		tab.addDouble("front left", () -> getPosition(0));
		tab.addDouble("front right", () -> getPosition(1));
		tab.addDouble("back left", () -> getPosition(2));
		tab.addDouble("back right", () -> getPosition(3));

		facingRequest = new SwerveRequest.FieldCentricFacingAngle()
		.withSteerRequestType(SteerRequestType.MotionMagic);
		facingRequest.HeadingController = new PhoenixPIDController(Math.toRadians(45), 0, 0);
	}

	private void setLimits(int module) {
		drivetrain.getModule(0).getDriveMotor()
		.getConfigurator().apply(new CurrentLimitsConfigs()
		.withSupplyCurrentLimit(80)
		.withSupplyCurrentLimitEnable(true));
	}

	final SwerveRequest.FieldCentricFacingAngle facingRequest;

	public void turnToFace(Rotation2d rotation) {
		drivetrain.setControl(
			facingRequest.
				withTargetDirection(rotation)
			);
	}

	public void stop() {
		drivetrain.setControl(new SwerveRequest.Idle());
	}

    /**
     * A method that gets whether you are on red alliance. If there is not an alliance,
     * returns false
     * @return {@code true} if you are on the red alliance
     */
    private static boolean onRed() {
        return DriverStation.getAlliance()
            .<Boolean>map((j) -> j == Alliance.Red)
            .orElse(false);
    }

    public Rotation2d getRotation() {
        return drivetrain.getRotation3d().toRotation2d();
    }

    public Pose2d getPose() {
        return drivetrain.getState().Pose;
    }

    public void enableSlowMode(boolean enable) {
        multiplier = enable ? 0.4 : 1;
    }

	private final SwerveRequest.FieldCentric xyrRequest =
		new SwerveRequest.FieldCentric()
				.withDriveRequestType(DriveRequestType.OpenLoopVoltage)
				.withSteerRequestType(SteerRequestType.MotionMagicExpo);

	public void drive(double x, double y, double rotation) {
		drivetrain.setControl(
			xyrRequest.withVelocityX(x * multiplier)
				.withVelocityY(y * multiplier)
				.withRotationalRate(rotation)
		);
		
	}

	private final SwerveRequest.ApplyChassisSpeeds chassisSpeedsRequest =
		new SwerveRequest.ApplyChassisSpeeds().withDriveRequestType(DriveRequestType.OpenLoopVoltage)
			.withSteerRequestType(SteerRequestType.MotionMagicExpo);

    public void drive(ChassisSpeeds demand) {
        drivetrain.setControl(chassisSpeedsRequest.withSpeeds(demand));
    }

    public void resetOdometry(Pose2d currentPose) {
		drivetrain.seedFieldRelative(currentPose);
    }

	public void orientForward() {
		drivetrain.seedFieldRelative();
	}

	private double getPosition(int moduleId) {
		return drivetrain.getModule(moduleId).getCANcoder().getAbsolutePosition().getValueAsDouble();
	}

	public Pose3d get3DPose() {
		return new Pose3d(getPose());
	}

	public SwerveConfig getOffsets() {
		return new SwerveConfig(
			-getPosition(0),
			-getPosition(1),
			-getPosition(2),
			-getPosition(3)
			);
	}

	Field2d field = new Field2d();

	/**
	 * Update position of robot
	 * @param pose the position of the robot
	 */
	public void addMeasurement(Pose2d pose) {
		double timestamp = Timer.getFPGATimestamp();
		OptionalLong msDelay = limelight.getLocationalData().lastMSDelay();
		if (msDelay.isPresent()) {
			timestamp -= msDelay.getAsLong() / 1000.0;
		}
		drivetrain.addVisionMeasurement(pose, timestamp);
	}

	private static final Translation2d poseOffset = new Translation2d(8.310213, 4.157313);

	private boolean useLimelightOffset = false;

	private static Pose2d offsetPose(Pose2d pose) {
		double x = pose.getX() + poseOffset.getX();
		double y = pose.getY() + poseOffset.getY();
		return new Pose2d(x, y, pose.getRotation());
	}

	@Override
	public void periodic() {
		
		SmartDashboard.putData(field);
		// if we have a position from the robot, and we arx`e in teleop, update our pose
		if (limelight.hasTarget() && DriverStation.isTeleopEnabled()) {
			limelight.getLocationalData().getBotpose()
			.map(Pose3d::toPose2d).ifPresent(this::addMeasurement);
			useLimelightOffset = true;
		}
		if (useLimelightOffset) {
			field.setRobotPose(offsetPose(getPose()));
		} else {
			field.setRobotPose(getPose());
		}
	}
}
