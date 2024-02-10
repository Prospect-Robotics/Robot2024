package com.team2813.subsystems;

import com.ctre.phoenix.sensors.Pigeon2;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
import com.swervedrivespecialties.swervelib.Mk4iSwerveModuleHelper.GearRatio;
import com.swervedrivespecialties.swervelib.SdsModuleConfigurations;

import com.team2813.lib2813.control.imu.Pigeon2Wrapper;
import com.team2813.lib2813.swerve.controllers.SwerveModule;
import com.team2813.lib2813.swerve.helpers.Mk4iSwerveModuleHelper;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static com.team2813.Constants.*;

public class Drive extends SubsystemBase {
    private static final double TRACKWIDTH = 1e-8;
    private static final double WHEELBASE = 1e-8;

    public static final double MAX_VELOCITY = 6380.0 / 60.0 *
            SdsModuleConfigurations.MK4I_L2.getDriveReduction() *
            SdsModuleConfigurations.MK4I_L2.getWheelDiameter() * Math.PI; // m/s
    public static final double MAX_ANGULAR_VELOCITY = MAX_VELOCITY / Math.hypot(TRACKWIDTH / 2, WHEELBASE / 2); // radians per second


    

    private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
            // Front Left
            new Translation2d(TRACKWIDTH / 2, WHEELBASE / 2),
            // Front Right
            new Translation2d(TRACKWIDTH / 2, -WHEELBASE / 2),
            // Back Left
            new Translation2d(-TRACKWIDTH / 2, WHEELBASE / 2),
            // Back Right
            new Translation2d(-TRACKWIDTH / 2, -WHEELBASE / 2)
    );

    private SwerveDriveOdometry odometry;

    private final SwerveModule frontLeftModule;
    private final SwerveModule frontRightModule;
    private final SwerveModule backLeftModule;
    private final SwerveModule backRightModule;

    private final Pigeon2Wrapper pigeon = new Pigeon2Wrapper(PIGEON_ID);

    private ChassisSpeeds chassisSpeedDemand = new ChassisSpeeds(0, 0, 0);

    private double multiplier = 1;

    public Drive() {
        String canbus = "swerve";

        double frontLeftSteerOffset = -Math.toRadians(0);
        double frontRightSteerOffset = -Math.toRadians(0);
        double backLeftSteerOffset = -Math.toRadians(0);
        double backRightSteerOffset = -Math.toRadians(0);

        // Module PID
        double kP = 0.2;
        double kI = 0;
        double kD = 0;

        ShuffleboardTab tab = Shuffleboard.getTab("Drivetrain");

        frontLeftModule = Mk4iSwerveModuleHelper.createFalcon500(
                tab.getLayout("Front Left Module", BuiltInLayouts.kList)
                        .withSize(2, 4).withPosition(0, 0),
                GearRatio.L2,
                canbus,
                FRONT_LEFT_DRIVE_ID,
                FRONT_LEFT_STEER_ID,
                FRONT_LEFT_ENCODER_ID,
                kP,
                kI,
                kD,
                frontLeftSteerOffset
        );

        frontRightModule = Mk4iSwerveModuleHelper.createFalcon500(
                tab.getLayout("Front Right Module", BuiltInLayouts.kList)
                        .withSize(2, 4).withPosition(2, 0),
                GearRatio.L2,
                canbus,
                FRONT_RIGHT_DRIVE_ID,
                FRONT_RIGHT_STEER_ID,
                FRONT_RIGHT_ENCODER_ID,
                kP,
                kI,
                kD,
                frontRightSteerOffset
        );
        
        backLeftModule = Mk4iSwerveModuleHelper.createFalcon500(
                tab.getLayout("Back Left Module", BuiltInLayouts.kList)
                        .withSize(2, 4).withPosition(4, 0),
                GearRatio.L2,
                canbus,
                BACK_LEFT_DRIVE_ID,
                BACK_LEFT_STEER_ID,
                BACK_LEFT_ENCODER_ID,
                kP,
                kI,
                kD,
                backLeftSteerOffset
        );

        backRightModule = Mk4iSwerveModuleHelper.createFalcon500(
                tab.getLayout("Back Right Module", BuiltInLayouts.kList)
                        .withSize(2, 4).withPosition(6, 0),
                GearRatio.L2,
                canbus,
                BACK_RIGHT_DRIVE_ID,
                BACK_RIGHT_STEER_ID,
                BACK_RIGHT_ENCODER_ID,
                kP,
                kI,
                kD,
                backRightSteerOffset
        );

        pigeon.configMountPose(Pigeon2.AxisDirection.PositiveY, Pigeon2.AxisDirection.PositiveZ);

        AutoBuilder.configureHolonomic(
            this::getPose,
            this::resetOdometry,
            this::getChassisSpeeds,
            this::drive,
            new HolonomicPathFollowerConfig(
                new PIDConstants(0, 0, 0), // Translation PID
                new PIDConstants(0, 0, 0), // Rotation PID
                MAX_VELOCITY,
                0.4,
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
                frontLeftModule.getState(),
                frontRightModule.getState(),
                backLeftModule.getState(),
                backRightModule.getState()
        );
    }

    public void enableSlowMode(boolean enable) {
        multiplier = enable ? 0.4 : 1;
    }

    public void drive(ChassisSpeeds demand) {
        chassisSpeedDemand = demand;
    }

    public void resetOdometry(Pose2d currentPose) {
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
    }

    @Override
    public void periodic() {
        pigeon.periodicResetCheck();
        SmartDashboard.putNumber("Current Heading (degrees)", pigeon.getHeading());

        if (odometry != null) {
            SwerveModulePosition[] modulePositions = {
                    frontLeftModule.getPosition(),
                    frontRightModule.getPosition(),
                    backLeftModule.getPosition(),
                    backRightModule.getPosition()
            };

            odometry.update(getRotation(), modulePositions);

            SmartDashboard.putString("Current Pose", odometry.getPoseMeters().toString());
        }

        SwerveModuleState[] moduleStates = kinematics.toSwerveModuleStates(chassisSpeedDemand);
        SwerveDriveKinematics.desaturateWheelSpeeds(moduleStates, MAX_VELOCITY);

        frontLeftModule.set(moduleStates[0].speedMetersPerSecond * multiplier, moduleStates[0].angle.getRadians());
        frontRightModule.set(moduleStates[1].speedMetersPerSecond * multiplier, moduleStates[1].angle.getRadians());
        backLeftModule.set(moduleStates[2].speedMetersPerSecond * multiplier, moduleStates[2].angle.getRadians());
        backRightModule.set(moduleStates[3].speedMetersPerSecond * multiplier, moduleStates[3].angle.getRadians());
    }
}
