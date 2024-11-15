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
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
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
			.withKP(2.5).withKI(0).withKD(0)
			.withKS(0).withKV(0).withKA(0);
			
		SwerveDrivetrainConstants drivetrainConstants = new SwerveDrivetrainConstants()
			.withPigeon2Id(PIGEON_ID)
			.withCANbusName(RobotSpecificConfigs.swerveCanbus());
		SwerveModuleConstantsFactory constantCreator = new SwerveModuleConstantsFactory()
			.withDriveMotorGearRatio(6.75)
			.withSteerMotorGearRatio(150.0 / 7)
			.withWheelRadius(1.75)
			.withSlipCurrent(90) // tune :)
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
		// for (int i = 0; i < 4; i++) {
		// 	setLimits(i);
		// }
		AutoBuilder.configureHolonomic(
			this::getAutoPose,
			this::resetOdometry,
			drivetrain::getChassisSpeeds,
			this::drive,
			new HolonomicPathFollowerConfig(
				new PIDConstants(2.1, 0, 0), // Translation PID
				new PIDConstants(0.8, 0, 0), // Rotation PID
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
		.withDriveRequestType(DriveRequestType.Velocity)
		.withSteerRequestType(SteerRequestType.MotionMagic);
		facingRequest.HeadingController = new PhoenixPIDController(3.5, 0, 0);
		Shuffleboard.getTab("swerve").add("rotation PID", facingRequest.HeadingController);
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

    public Pose2d getTeleopPose() {
        if (useLimelightOffset) {
			return drivetrain.getState().Pose;
		} else {
			return offsetTeleopPose(drivetrain.getState().Pose);
		}
    }

	public Pose2d getAutoPose() {
		if (useLimelightOffset) {
			return offsetAutoPose(drivetrain.getState().Pose);
		} else {
			return drivetrain.getState().Pose;
		}
	}

    public void enableSlowMode(boolean enable) {
        multiplier = enable ? 0.4 : 1;
    }

	private final SwerveRequest.FieldCentric xyrRequest =
		new SwerveRequest.FieldCentric()
				.withDriveRequestType(DriveRequestType.OpenLoopVoltage)
				.withSteerRequestType(SteerRequestType.MotionMagicExpo);

	boolean correctRotation = false;
	public void drive(double x, double y, double rotation) {
		double multiplier = onRed() && correctRotation ? this.multiplier * -1 : this.multiplier;
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
		useLimelightOffset = false;
		correctRotation = true;
		drivetrain.seedFieldRelative(currentPose);
    }

	public void orientForward() {
		drivetrain.seedFieldRelative();
	}

	private double getPosition(int moduleId) {
		return drivetrain.getModule(moduleId).getCANcoder().getAbsolutePosition().getValueAsDouble();
	}

	public Pose3d get3DPose() {
		return new Pose3d(getTeleopPose());
	}

	public SwerveConfig getOffsets() {
		return new SwerveConfig(
			getPosition(0),
			getPosition(1),
			getPosition(2),
			getPosition(3)
			);
	}

	Field2d field = new Field2d();

	private static final Translation2d poseOffset = new Translation2d(8.310213, 4.157313);

	private boolean useLimelightOffset = false;

	private static Pose2d offsetAutoPose(Pose2d pose) {
		double x = pose.getX() + poseOffset.getX();
		double y = pose.getY() + poseOffset.getY();
		return new Pose2d(x, y, pose.getRotation());
	}

	private static Pose2d offsetTeleopPose(Pose2d pose) {
		double x = pose.getX() - poseOffset.getX();
		double y = pose.getY() - poseOffset.getY();
		return new Pose2d(x, y, pose.getRotation());
	}

	private void updatePosition(Pose3d pose) {
		if (!useLimelightOffset) {
			drivetrain.seedFieldRelative(pose.toPose2d());
		} else {
			drivetrain.addVisionMeasurement(pose.toPose2d(), limelight.getCaptureLatency().orElse(0));
		}
		useLimelightOffset = true;
		correctRotation = true;
	}
	
	StructArrayPublisher<SwerveModuleState> expextedState = 
		NetworkTableInstance.getDefault().getStructArrayTopic("expected state", SwerveModuleState.struct).publish();
	StructArrayPublisher<SwerveModuleState> actualState = 
		NetworkTableInstance.getDefault().getStructArrayTopic("actual state", SwerveModuleState.struct).publish();
	StructPublisher<Rotation2d> rotation = 
		NetworkTableInstance.getDefault().getStructTopic("rotation", Rotation2d.struct).publish();
	StructPublisher<Pose2d> currentPose =
		NetworkTableInstance.getDefault().getStructTopic("current pose", Pose2d.struct).publish();

	@Override
	public void periodic() {
		
		SmartDashboard.putData(field);
		SmartDashboard.putString("json", limelight.getJsonDump().map(Object::toString).orElse("NONE"));
		limelight.getLocationalData().getBotpose().ifPresent(this::updatePosition);
		// if we have a position from the robot, and we arx`e in teleop, update our pose
		expextedState.set(drivetrain.getState().ModuleTargets);
		actualState.set(drivetrain.getState().ModuleStates);
		rotation.set(getRotation());
		currentPose.set(getAutoPose());

		field.setRobotPose(getAutoPose());

		for (int i = 0; i < 4; i++) {
			double temp = drivetrain.getModule(i).getDriveMotor().getDeviceTemp().getValueAsDouble();
			SmartDashboard.putNumber(String.format("Swerve module number %d", i), temp);
		}
	}
}
