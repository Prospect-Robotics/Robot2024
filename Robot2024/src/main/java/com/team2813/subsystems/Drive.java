package com.team2813.subsystems;

<<<<<<< HEAD
=======
import static com.team2813.Constants.*;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.*;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModuleConstants.*;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModuleConstantsFactory;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
>>>>>>> beec593 (possibly done)
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
import com.swervedrivespecialties.swervelib.SdsModuleConfigurations;
import com.team2813.lib2813.control.imu.Pigeon2Wrapper;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drive extends SubsystemBase {
    private static final double TRACKWIDTH = 1e-8;
    private static final double WHEELBASE = 1e-8;

    public static final double MAX_VELOCITY = 6380.0 / 60.0 *
            SdsModuleConfigurations.MK4I_L2.getDriveReduction() *
            SdsModuleConfigurations.MK4I_L2.getWheelDiameter() * Math.PI; // m/s
    public static final double MAX_ANGULAR_VELOCITY = MAX_VELOCITY / Math.hypot(TRACKWIDTH / 2, WHEELBASE / 2); // radians per second
	
    private final SwerveDriveKinematics kinematics;

    private SwerveDriveOdometry odometry;

    private final Pigeon2Wrapper pigeon = new Pigeon2Wrapper(PIGEON_ID, "swerve");

    private double multiplier = 1;

	SwerveDrivetrain drivetrain;

    public Drive() {
		// rotations
        double frontLeftSteerOffset = 0;
        double frontRightSteerOffset = 0;
        double backLeftSteerOffset = 0;
        double backRightSteerOffset = 0;

		// tune
		Slot0Configs steerGains = new Slot0Configs()
			.withKP(100).withKI(0).withKD(0.2)
			.withKS(0).withKV(1.5).withKA(0);
		// tune
		Slot0Configs driveGains = new Slot0Configs()
			.withKP(3).withKI(0).withKD(0)
			.withKS(0).withKV(0).withKA(0);

		SwerveDrivetrainConstants drivetrainConstants = new SwerveDrivetrainConstants()
			.withPigeon2Id(PIGEON_ID)
			.withCANbusName("swerve");
		SwerveModuleConstantsFactory constantCreator = new SwerveModuleConstantsFactory()
			.withDriveMotorGearRatio(6.75)
			.withWheelRadius(1.75) // get actual value
			.withSlipCurrent(300) // tune :)
			.withSteerMotorGains(steerGains)
			.withDriveMotorGains(driveGains)
			.withSteerMotorClosedLoopOutput(ClosedLoopOutputType.Voltage)
			.withDriveMotorClosedLoopOutput(ClosedLoopOutputType.TorqueCurrentFOC)
			.withSpeedAt12VoltsMps(4.78536)
			.withFeedbackSource(SteerFeedbackType.FusedCANcoder)
			.withCouplingGearRatio(3.5) // tune :P
			.withSteerMotorInverted(false); // idk what this needs to be :(
		double frontDist = 10; // x
		double leftDist = 10; // y
		SwerveModuleConstants frontLeft = constantCreator.createModuleConstants(
			FRONT_LEFT_STEER_ID, FRONT_LEFT_DRIVE_ID,
			FRONT_LEFT_ENCODER_ID, frontLeftSteerOffset,
			Units.inchesToMeters(leftDist), Units.inchesToMeters(frontDist),
			false);
		SwerveModuleConstants frontRight = constantCreator.createModuleConstants(
			FRONT_RIGHT_STEER_ID, FRONT_RIGHT_DRIVE_ID,
			FRONT_RIGHT_ENCODER_ID, frontRightSteerOffset,
			Units.inchesToMeters(-leftDist), Units.inchesToMeters(frontDist),
			false
		);
		SwerveModuleConstants backLeft = constantCreator.createModuleConstants(
			BACK_LEFT_STEER_ID, BACK_LEFT_DRIVE_ID,
			BACK_LEFT_ENCODER_ID, backLeftSteerOffset,
			Units.inchesToMeters(leftDist), Units.inchesToMeters(-frontDist),
			false
		);
		SwerveModuleConstants backRight = constantCreator.createModuleConstants(
			BACK_RIGHT_STEER_ID, BACK_RIGHT_DRIVE_ID,
			BACK_RIGHT_ENCODER_ID, backRightSteerOffset,
			Units.inchesToMeters(-leftDist), Units.inchesToMeters(-frontDist),
			false
		);
		SwerveModuleConstants[] constants = new SwerveModuleConstants[]{frontLeft, frontRight, backLeft, backRight};
		Translation2d[] locations = new Translation2d[constants.length];
		for (int i = 0; i < constants.length; i++) {
			locations[i] = new Translation2d(constants[i].LocationX, constants[i].LocationY);
		}
		kinematics = new SwerveDriveKinematics(locations);
		drivetrain = new SwerveDrivetrain(drivetrainConstants, frontLeft, frontRight, backLeft, backRight);
		AutoBuilder.configureHolonomic(
			this::getPose,
			this::resetOdometry,
			this::getChassisSpeeds,
			this::drive,
			new HolonomicPathFollowerConfig(
				new PIDConstants(0.1, 0, 0), // Translation PID
				new PIDConstants(0, 0, 0), // Rotation PID
				MAX_VELOCITY,
				0.410178,
				new ReplanningConfig()
				),
			Drive::onRed,
			this);
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
        return Rotation2d.fromDegrees(pigeon.getHeading());
    }

    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    public Pigeon2Wrapper getPigeon() {
        return pigeon;
    }

    public ChassisSpeeds getChassisSpeeds() {
        return kinematics.toChassisSpeeds(
                drivetrain.getState().ModuleStates
        );
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
<<<<<<< HEAD
        SwerveModulePosition[] modulePositions = {
                frontLeftModule.getPosition(),
                frontRightModule.getPosition(),
                backLeftModule.getPosition(),
                backRightModule.getPosition()
        };

        pigeon.setHeading(currentPose.getRotation().getDegrees());
        if (odometry == null) {
            odometry = new SwerveDriveOdometry(kinematics, currentPose.getRotation(), modulePositions, currentPose);
        } else {
            odometry.resetPosition(Rotation2d.fromDegrees(pigeon.getHeading()), modulePositions, currentPose);
        }
=======
		drivetrain.seedFieldRelative(currentPose);
>>>>>>> beec593 (possibly done)
    }

    @Override
    public void periodic() {
        pigeon.periodicResetCheck();
        SmartDashboard.putNumber("Current Heading (degrees)", pigeon.getHeading());
    }
}
