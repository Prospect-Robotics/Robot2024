package com.team2813.lib2813.util;

import java.util.function.Supplier;

import com.ctre.phoenix.ErrorCode;

import edu.wpi.first.wpilibj.DriverStation;

public class ConfigUtils {
	// make class non-instantiable
	private ConfigUtils() {
		throw new AssertionError("cannot create ConfigUtils instance");
	}

	private static final int ATTEMPTS = 10;

	public static void ctreConfig(Supplier<ErrorCode> configMethod) {
		ErrorCode errorCode = configMethod.get();
		if (errorCode != ErrorCode.OK) {
			DriverStation.reportError(String.format("%s: %s", "Config Attempt 1 Failed", errorCode.toString()), false);
			for (int i = 1; i < ATTEMPTS; i++) {
				errorCode = configMethod.get();
				if (errorCode == ErrorCode.OK) {
					DriverStation.reportWarning("Config Success!", false);
					return;
				} else {
					DriverStation.reportError(
							String.format("%s: %s", "Config Attempt " + (i + 1) + " Failed", errorCode.toString()),
							false);
				}
			}
			DriverStation.reportError(String.format("%s: %s", "Config Failed", errorCode.toString()), false);
		}
	}
}
