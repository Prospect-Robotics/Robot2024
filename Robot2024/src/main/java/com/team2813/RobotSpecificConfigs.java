package com.team2813;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.ClosedLoopOutputType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModuleConstants.SteerFeedbackType;

import edu.wpi.first.wpilibj.DriverStation;

public class RobotSpecificConfigs {
	public static class SwerveConfig implements Serializable {
		private final double frontLeft;
		private final double frontRight;
		private final double backLeft;
		private final double backRight;
		/**
		 * Creates a SwerveConfig for recalibrating
		 */
		public SwerveConfig() {
			this(0, 0, 0, 0);
		}
		public double frontLeftOffset() {
			return frontLeft;
		}
		public double frontRightOffset() {
			return frontRight;
		}
		public double backLeftOffset() {
			return backLeft;
		}
		public double backRightOffset() {
			return backRight;
		}
		public SwerveConfig add(SwerveConfig other) {
			double frontRight = other.frontRight + this.frontRight;
			double frontLeft = other.frontLeft + this.frontLeft;
			double backLeft = other.backLeft + this.backLeft;
			double backRight = other.backRight + this.backRight;
			return new SwerveConfig(frontLeft, frontRight, backLeft, backRight);
		}
		public SwerveConfig negate() {
			return new SwerveConfig(-frontLeft, -frontRight, -backLeft, -backRight);
		}
		/**
		 * Creates a new SwerveConfig with their module offsets
		 * @param frontLeft front left offset
		 * @param frontRight front right offset
		 * @param backLeft back left offset
		 * @param backRight back right offset
		 * @throws IllegalArgumentException if an offset is larger than a full circle
		 */
		public SwerveConfig(double frontLeft, double frontRight, double backLeft, double backRight) {
			String message = "%s offset cannot be larger than a full circle";
			if (Math.abs(frontLeft) > 1) {
				throw new IllegalArgumentException(String.format(message, "front left"));
			} else if (Math.abs(frontRight) > 1) {
				throw new IllegalArgumentException(String.format(message, "front right"));
			} else if (Math.abs(backLeft) > 1) {
				throw new IllegalArgumentException(String.format(message, "back left"));
			} else if (Math.abs(backRight) > 1) {
				throw new IllegalArgumentException(String.format(message, "back right"));
			}
			this.frontLeft = frontLeft;
			this.frontRight = frontRight;
			this.backLeft = backLeft;
			this.backRight = backRight;
		}
		private Object writeReplace() {
			return new SerializationProxy(this);
		}
		private void readObject(ObjectInputStream stream) throws InvalidObjectException {
			throw new InvalidObjectException("Proxy required");
		}
		@Override
		public boolean equals(Object other) {
			if (!(other instanceof SwerveConfig)) {
				return false;
			}
			SwerveConfig o = (SwerveConfig) other;
			return frontLeft == o.frontLeft && frontRight == o.frontRight &&
				backLeft == o.backLeft && backRight == o.backRight;
		}
		private static class SerializationProxy implements Serializable {
			private final double frontLeft;
			private final double frontRight;
			private final double backLeft;
			private final double backRight;
			private static final long serialVersionUID = 3142134904683214L;
			SerializationProxy(SwerveConfig obj) {
				this.frontLeft = obj.frontLeft;
				this.frontRight = obj.frontRight;
				this.backLeft = obj.backLeft;
				this.backRight = obj.backRight;
			}
			private Object readResolve() {
				return new SwerveConfig(frontLeft, frontRight, backLeft, backRight);
			}
		}
	}
	private static volatile boolean loaded = false;

	private static String swerveCanbus = "swerve";
	private static ClosedLoopOutputType driveOutput = ClosedLoopOutputType.TorqueCurrentFOC;
	private static SteerFeedbackType swerveFeedback = SteerFeedbackType.FusedCANcoder;
	private static SwerveConfig swerveConfig = new SwerveConfig();
	private static final Path swerveConfigPath = Path.of("/home", "lvuser", "swerveConfig.txt");
	private static boolean loadedSwerveConfig;
	private static boolean debugInfo;

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

	public static SwerveConfig swerveConfig() {
		checkLoaded();
		return swerveConfig;
	}

	public static boolean loadedSwerveConfig() {
		checkLoaded();
		return loadedSwerveConfig;
	}

	public static boolean debug() {
		checkLoaded();
		return debugInfo;
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

	public static void saveSwerveConfig(SwerveConfig cnf) {
		// make sure file is read from first to make it impossible
		// to write and read the file at the same time
		checkLoaded();
		try (
			FileOutputStream fos = new FileOutputStream(swerveConfigPath.toFile());
			ObjectOutputStream stream = new ObjectOutputStream(fos)
		) {
			stream.writeObject(cnf);
		} catch (IOException e) {
			DriverStation.reportError("Failed writing file", false);
		}
	}

	public static void resetSwerveConfig() {
		// make sure file is read from first to make it impossible
		// to write and read the file at the same time
		checkLoaded();
		swerveConfigPath.toFile().delete();
	}

	private static void load() {
		// DO NOT DELETE LINE BELOW
		loaded = true;
		// potentially do more expensive work, like loading a file
		// driveOutput = ClosedLoopOutputType.Voltage;
		// swerveFeedback = SteerFeedbackType.RemoteCANcoder;

		if (Files.exists(swerveConfigPath)) {
			try (
				FileInputStream fis = new FileInputStream(swerveConfigPath.toFile());
				ObjectInputStream stream = new ObjectInputStream(fis)
			) {
				Object obj = stream.readObject();
				if (obj instanceof SwerveConfig) {
					swerveConfig = (SwerveConfig) obj;
				} else {
					DriverStation.reportError("swerve config file was not right type", false);
				}
			} catch (IOException | ClassNotFoundException e) {
				DriverStation.reportError("failed loading swerve config file", false);
			}
			loadedSwerveConfig = true;
		}

		debugInfo = DriverStation.getEventName() == "";
	}
}
