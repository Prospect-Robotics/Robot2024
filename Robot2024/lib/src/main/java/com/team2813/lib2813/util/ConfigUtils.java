package com.team2813.lib2813.util;

import java.util.function.Supplier;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix6.StatusCode;
import com.revrobotics.REVLibError;

import edu.wpi.first.wpilibj.DriverStation;

public class ConfigUtils {
	// make class non-instantiable
	private ConfigUtils() {
		throw new AssertionError("cannot create ConfigUtils instance");
	}

	private static final int ATTEMPTS = 10;

	public static void revConfig(Supplier<REVLibError> configMethod) {
		REVLibError errorCode = configMethod.get();
		for (int i = 1; i <= ATTEMPTS && errorCode != REVLibError.kOk; i++) {
			DriverStation.reportError(String.format("%s: Config Attempt %d Failed", errorCode.toString(), i), false);
			errorCode = configMethod.get();
		}
		if (errorCode != REVLibError.kOk) {
			DriverStation.reportError(String.format("%s: Config Failed", errorCode.toString()), false);
		}
	}

	public static void phoenix6Config(Supplier<StatusCode> configMethod) {
		StatusCode errorCode = configMethod.get();
		for (int i = 1; i <= ATTEMPTS && errorCode != StatusCode.OK; i++) {
			DriverStation.reportError(String.format("%s: Config Attempt %d Failed", errorCode.toString(), i), false);
			errorCode = configMethod.get();
		}
		if (errorCode != StatusCode.OK) {
			DriverStation.reportError(String.format("%s: Config Failed", errorCode.toString()), false);
		}
	}

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
