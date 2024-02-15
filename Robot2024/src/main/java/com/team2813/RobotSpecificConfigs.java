package com.team2813;

import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.ClosedLoopOutputType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModuleConstants.SteerFeedbackType;

public class RobotSpecificConfigs {
	private static volatile boolean loaded = false;

	private static String swerveCanbus = "swerve";
	private static ClosedLoopOutputType driveOutput = ClosedLoopOutputType.TorqueCurrentFOC;
	private static SteerFeedbackType swerveFeedback = SteerFeedbackType.FusedCANcoder;

	public static String swerveCanbus() {
		checkLoaded();
		return swerveCanbus;
	}

	public static ClosedLoopOutputType driveOutput() {
		checkLoaded();
		return driveOutput;
	}

	public static SteerFeedbackType swerveFeedback() {
		checkLoaded();
		return swerveFeedback;
	}

	private static void checkLoaded() {
		if (!loaded) {
			synchronized (RobotSpecificConfigs.class) {
				if (!loaded) {
					load();
				}
			}
		}
	}

	private static void load() {
		// DO NOT DELETE LINE BELOW
		loaded = true;
		// potentially do more expensive work, like loading a file
		driveOutput = ClosedLoopOutputType.Voltage;
		swerveFeedback = SteerFeedbackType.RemoteCANcoder;
	}
}
