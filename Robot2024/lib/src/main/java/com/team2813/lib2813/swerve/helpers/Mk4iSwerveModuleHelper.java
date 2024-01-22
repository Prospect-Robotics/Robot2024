package com.team2813.lib2813.swerve.helpers;

import com.swervedrivespecialties.swervelib.Mk4ModuleConfiguration;
import com.swervedrivespecialties.swervelib.Mk4iSwerveModuleHelper.GearRatio;
import com.swervedrivespecialties.swervelib.ctre.CanCoderAbsoluteConfiguration;
import com.team2813.lib2813.swerve.controllers.SwerveModule;
import com.team2813.lib2813.swerve.controllers.drive.Falcon500DriveController;
import com.team2813.lib2813.swerve.controllers.drive.NeoDriveController;
import com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerController;
import com.team2813.lib2813.swerve.controllers.steer.NeoSteerController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

public class Mk4iSwerveModuleHelper {
	private Mk4iSwerveModuleHelper() {
		throw new AssertionError("Mk4iSwerveModuleHelper not instationable");
	}

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            ShuffleboardLayout container,
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, gearRatio.getConfiguration(), configuration);
        driveController.addDashboardEntries(container);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );
        steerController.addDashboardEntries(container);

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param canbus           Name of the CANbus; can be a SocketCAN interface (on Linux),
     *      *                  or a CANivore device name or serial number.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the CANivore is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            ShuffleboardLayout container,
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            String canbus,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, canbus, gearRatio.getConfiguration(), configuration);
        driveController.addDashboardEntries(container);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        canbus,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );
        steerController.addDashboardEntries(container);

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            ShuffleboardLayout container,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        return createFalcon500(
                container,
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param gearRatio        The gearing configuration the module is in.
     * @param canbus           Name of the CANbus; can be a SocketCAN interface (on Linux),
     *      *                  or a CANivore device name or serial number.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the CANivore is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            ShuffleboardLayout container,
            GearRatio gearRatio,
            String canbus,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        return createFalcon500(
                container,
                new Mk4ModuleConfiguration(),
                gearRatio,
                canbus,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     *
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, gearRatio.getConfiguration(), configuration);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     *
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param canbus           Name of the CANbus; can be a SocketCAN interface (on Linux),
     *      *                  or a CANivore device name or serial number.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the CANivore is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            String canbus,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, canbus, gearRatio.getConfiguration(), configuration);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        canbus,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     *
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        return createFalcon500(
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     *
     * @param gearRatio        The gearing configuration the module is in.
     * @param canbus           Name of the CANbus; can be a SocketCAN interface (on Linux),
     *      *                  or a CANivore device name or serial number.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the CANivore is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            GearRatio gearRatio,
            String canbus,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        return createFalcon500(
                new Mk4ModuleConfiguration(),
                gearRatio,
                canbus,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            ShuffleboardLayout container,
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD);
        driveController.addDashboardEntries(container);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );
        steerController.addDashboardEntries(container);

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param canbus           Name of the CANbus; can be a SocketCAN interface (on Linux),
     *      *                  or a CANivore device name or serial number.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the CANivore is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            ShuffleboardLayout container,
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            String canbus,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, canbus, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD);
        driveController.addDashboardEntries(container);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        canbus,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );
        steerController.addDashboardEntries(container);

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            ShuffleboardLayout container,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        return createFalcon500(
                container,
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param gearRatio        The gearing configuration the module is in.
     * @param canbus           Name of the CANbus; can be a SocketCAN interface (on Linux),
     *      *                  or a CANivore device name or serial number.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the CANivore is licensed
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            ShuffleboardLayout container,
            GearRatio gearRatio,
            String canbus,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        return createFalcon500(
                container,
                new Mk4ModuleConfiguration(),
                gearRatio,
                canbus,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     *
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     *
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param canbus           Name of the CANbus; can be a SocketCAN interface (on Linux),
     *      *                  or a CANivore device name or serial number.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the CANivore is licensed
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            String canbus,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, canbus, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        canbus,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     *
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        return createFalcon500(
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     *
     * @param gearRatio        The gearing configuration the module is in.
     * @param canbus           Name of the CANbus; can be a SocketCAN interface (on Linux),
     *      *                  or a CANivore device name or serial number.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the CANivore is licensed
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            GearRatio gearRatio,
            String canbus,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        return createFalcon500(
                new Mk4ModuleConfiguration(),
                gearRatio,
                canbus,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            ShuffleboardLayout container,
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD)
                .withFeedforward(driveFeedforward);
        driveController.addDashboardEntries(container);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );
        steerController.addDashboardEntries(container);

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param canbus           Name of the CANbus; can be a SocketCAN interface (on Linux),
     *      *                  or a CANivore device name or serial number.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the CANivore is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            ShuffleboardLayout container,
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            String canbus,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, canbus, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD)
                .withFeedforward(driveFeedforward);
        driveController.addDashboardEntries(container);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        canbus,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );
        steerController.addDashboardEntries(container);

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            ShuffleboardLayout container,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        return createFalcon500(
                container,
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                driveFeedforward,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param gearRatio        The gearing configuration the module is in.
     * @param canbus           Name of the CANbus; can be a SocketCAN interface (on Linux),
     *      *                  or a CANivore device name or serial number.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the CANivore is licensed
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            ShuffleboardLayout container,
            GearRatio gearRatio,
            String canbus,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        return createFalcon500(
                container,
                new Mk4ModuleConfiguration(),
                gearRatio,
                canbus,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                driveFeedforward,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     *
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD)
                .withFeedforward(driveFeedforward);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     *
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param canbus           Name of the CANbus; can be a SocketCAN interface (on Linux),
     *      *                  or a CANivore device name or serial number.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the CANivore is licensed
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            String canbus,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, canbus, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD)
                .withFeedforward(driveFeedforward);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        canbus,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     *
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        return createFalcon500(
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                driveFeedforward,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses Falcon 500s for driving and steering.
     *
     * @param gearRatio        The gearing configuration the module is in.
     * @param canbus           Name of the CANbus; can be a SocketCAN interface (on Linux),
     *      *                  or a CANivore device name or serial number.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the CANivore is licensed
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500(
            GearRatio gearRatio,
            String canbus,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        return createFalcon500(
                new Mk4ModuleConfiguration(),
                gearRatio,
                canbus,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                driveFeedforward,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses NEOs for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeo(
            ShuffleboardLayout container,
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        NeoDriveController driveController = new NeoDriveController(driveMotorPort, gearRatio.getConfiguration(), configuration);
        driveController.addDashboardEntries(container);

        NeoSteerController steerController = new NeoSteerController(
                new com.team2813.lib2813.swerve.controllers.steer.SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );
        steerController.addDashboardEntries(container);

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses NEOs for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeo(
            ShuffleboardLayout container,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        return createNeo(
                container,
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses NEOs for driving and steering.
     *
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeo(
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        NeoDriveController driveController = new NeoDriveController(driveMotorPort, gearRatio.getConfiguration(), configuration);

        NeoSteerController steerController = new NeoSteerController(
                new com.team2813.lib2813.swerve.controllers.steer.SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses NEOs for driving and steering.
     *
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeo(
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        return createNeo(
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses NEOs for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeo(
            ShuffleboardLayout container,
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        NeoDriveController driveController = new NeoDriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD);
        driveController.addDashboardEntries(container);

        NeoSteerController steerController = new NeoSteerController(
                new com.team2813.lib2813.swerve.controllers.steer.SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );
        steerController.addDashboardEntries(container);

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses NEOs for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeo(
            ShuffleboardLayout container,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        return createNeo(
                container,
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses NEOs for driving and steering.
     *
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeo(
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        NeoDriveController driveController = new NeoDriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD);

        NeoSteerController steerController = new NeoSteerController(
                new com.team2813.lib2813.swerve.controllers.steer.SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses NEOs for driving and steering.
     *
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeo(
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        return createNeo(
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses NEOs for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeo(
            ShuffleboardLayout container,
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        NeoDriveController driveController = new NeoDriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD)
                .withFeedforward(driveFeedforward);
        driveController.addDashboardEntries(container);

        NeoSteerController steerController = new NeoSteerController(
                new com.team2813.lib2813.swerve.controllers.steer.SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );
        steerController.addDashboardEntries(container);

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses NEOs for driving and steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeo(
            ShuffleboardLayout container,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        return createNeo(
                container,
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                driveFeedforward,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses NEOs for driving and steering.
     *
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeo(
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        NeoDriveController driveController = new NeoDriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD)
                .withFeedforward(driveFeedforward);

        NeoSteerController steerController = new NeoSteerController(
                new com.team2813.lib2813.swerve.controllers.steer.SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses NEOs for driving and steering.
     *
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeo(
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        return createNeo(
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                driveFeedforward,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses a Falcon 500 for driving and a NEO steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500Neo(
            ShuffleboardLayout container,
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, gearRatio.getConfiguration(), configuration);
        driveController.addDashboardEntries(container);

        NeoSteerController steerController = new NeoSteerController(
                new com.team2813.lib2813.swerve.controllers.steer.SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );
        steerController.addDashboardEntries(container);

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses a Falcon 500 for driving and a NEO steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500Neo(
            ShuffleboardLayout container,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        return createFalcon500Neo(
                container,
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses a Falcon 500 for driving and a NEO steering.
     *
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500Neo(
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, gearRatio.getConfiguration(), configuration);

        NeoSteerController steerController = new NeoSteerController(
                new com.team2813.lib2813.swerve.controllers.steer.SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses a Falcon 500 for driving and a NEO steering.
     *
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500Neo(
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        return createFalcon500Neo(
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses a Falcon 500 for driving and a NEO steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500Neo(
            ShuffleboardLayout container,
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD);
        driveController.addDashboardEntries(container);

        NeoSteerController steerController = new NeoSteerController(
                new com.team2813.lib2813.swerve.controllers.steer.SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );
        steerController.addDashboardEntries(container);

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses a Falcon 500 for driving and a NEO steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500Neo(
            ShuffleboardLayout container,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        return createFalcon500Neo(
                container,
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses a Falcon 500 for driving and a NEO steering.
     *
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500Neo(
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD);

        NeoSteerController steerController = new NeoSteerController(
                new com.team2813.lib2813.swerve.controllers.steer.SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses a Falcon 500 for driving and a NEO steering.
     *
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500Neo(
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        return createFalcon500Neo(
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses a Falcon 500 for driving and a NEO steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500Neo(
            ShuffleboardLayout container,
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD)
                .withFeedforward(driveFeedforward);
        driveController.addDashboardEntries(container);

        NeoSteerController steerController = new NeoSteerController(
                new com.team2813.lib2813.swerve.controllers.steer.SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );
        steerController.addDashboardEntries(container);

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses a Falcon 500 for driving and a NEO steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500Neo(
            ShuffleboardLayout container,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        return createFalcon500Neo(
                container,
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                driveFeedforward,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses a Falcon 500 for driving and a NEO steering.
     *
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500Neo(
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        Falcon500DriveController driveController = new Falcon500DriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD)
                .withFeedforward(driveFeedforward);

        NeoSteerController steerController = new NeoSteerController(
                new com.team2813.lib2813.swerve.controllers.steer.SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses a Falcon 500 for driving and a NEO steering.
     *
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive Falcon 500.
     * @param steerMotorPort   The CAN ID of the steer NEO.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @param isLicensed       Whether or not the drive motor is licensed.
     * @return The configured swerve module.
     */
    public static SwerveModule createFalcon500Neo(
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        return createFalcon500Neo(
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                driveFeedforward,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses a NEO for driving and a Falcon 500 for steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeoFalcon500(
            ShuffleboardLayout container,
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        NeoDriveController driveController = new NeoDriveController(driveMotorPort, gearRatio.getConfiguration(), configuration);
        driveController.addDashboardEntries(container);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );
        steerController.addDashboardEntries(container);

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses a NEO for driving and a Falcon 500 for steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeoFalcon500(
            ShuffleboardLayout container,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        return createNeoFalcon500(
                container,
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses a NEO for driving and a Falcon 500 for steering.
     *
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeoFalcon500(
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        NeoDriveController driveController = new NeoDriveController(driveMotorPort, gearRatio.getConfiguration(), configuration);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses a NEO for driving and a Falcon 500 for steering.
     *
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeoFalcon500(
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double steerOffset
    ) {
        return createNeoFalcon500(
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses a NEO for driving and a Falcon 500 for steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeoFalcon500(
            ShuffleboardLayout container,
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        NeoDriveController driveController = new NeoDriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD);
        driveController.addDashboardEntries(container);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );
        steerController.addDashboardEntries(container);

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses a NEO for driving and a Falcon 500 for steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeoFalcon500(
            ShuffleboardLayout container,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        return createNeoFalcon500(
                container,
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses a NEO for driving and a Falcon 500 for steering.
     *
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeoFalcon500(
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        NeoDriveController driveController = new NeoDriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses a NEO for driving and a Falcon 500 for steering.
     *
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeoFalcon500(
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            double steerOffset
    ) {
        return createNeoFalcon500(
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses a NEO for driving and a Falcon 500 for steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeoFalcon500(
            ShuffleboardLayout container,
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        NeoDriveController driveController = new NeoDriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD)
                .withFeedforward(driveFeedforward);
        driveController.addDashboardEntries(container);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );
        steerController.addDashboardEntries(container);

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses a NEO for driving and a Falcon 500 for steering.
     * Module information is displayed in the specified Shuffleboard container.
     *
     * @param container        The container to display module information in
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeoFalcon500(
            ShuffleboardLayout container,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        return createNeoFalcon500(
                container,
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                driveFeedforward,
                steerOffset
        );
    }

    /**
     * Creates a Mk4i swerve module that uses a NEO for driving and a Falcon 500 for steering.
     *
     * @param configuration    Module configuration parameters to use.
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeoFalcon500(
            Mk4ModuleConfiguration configuration,
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        NeoDriveController driveController = new NeoDriveController(driveMotorPort, gearRatio.getConfiguration(), configuration)
                .withPidConstants(drive_kP, drive_kI, drive_kD)
                .withFeedforward(driveFeedforward);

        Falcon500SteerController steerController = new Falcon500SteerController(
                new com.team2813.lib2813.swerve.controllers.steer.Falcon500SteerConfiguration(
                        steerMotorPort,
                        new CanCoderAbsoluteConfiguration(steerEncoderPort, steerOffset)
                ),
                gearRatio.getConfiguration(),
                configuration
        );

        return new SwerveModule(driveController, steerController);
    }

    /**
     * Creates a Mk4i swerve module that uses a NEO for driving and a Falcon 500 for steering.
     *
     * @param gearRatio        The gearing configuration the module is in.
     * @param driveMotorPort   The CAN ID of the drive NEO.
     * @param steerMotorPort   The CAN ID of the steer Falcon 500.
     * @param steerEncoderPort The CAN ID of the steer CANCoder.
     * @param drive_kP         The proportional gain for the driving PID.
     * @param drive_kI         The integral gain for the driving PID.
     * @param drive_kD         The derivative gain for the driving PID.
     * @param driveFeedforward Object to calculate feedforward added to driving PID.
     * @param steerOffset      The offset of the CANCoder in radians.
     * @return The configured swerve module.
     */
    public static SwerveModule createNeoFalcon500(
            GearRatio gearRatio,
            int driveMotorPort,
            int steerMotorPort,
            int steerEncoderPort,
            double drive_kP,
            double drive_kI,
            double drive_kD,
            SimpleMotorFeedforward driveFeedforward,
            double steerOffset
    ) {
        return createNeoFalcon500(
                new Mk4ModuleConfiguration(),
                gearRatio,
                driveMotorPort,
                steerMotorPort,
                steerEncoderPort,
                drive_kP,
                drive_kI,
                drive_kD,
                driveFeedforward,
                steerOffset
        );
    }
}