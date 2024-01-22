package com.team2813.lib2813.control.motors;

import static com.team2813.lib2813.util.InputValidation.checkCanId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.DeviceInformation;
import com.team2813.lib2813.control.PIDMotor;
import com.team2813.lib2813.util.ConfigUtils;
import com.team2813.lib2813.util.Units2813;

public class TalonFXWrapper implements PIDMotor {
	/**
	 * A TalonFX motor that can be equal to another one.
	 * This is used because TalonFXWrapper is not a TalonFX.
	 * package-private so it can be tested
	 */
	static class TalonMotor extends TalonFX {
		private DeviceInformation information;

		/**
		 * Create a TalonMotor on a specific canbus
		 * 
		 * @param canID  [0, 62] the can id of the motor
		 * @param canbus the canbus the motor is on
		 */
		public TalonMotor(int canID, String canbus) {
			super(checkCanId(canID), canbus);
			information = new DeviceInformation(canID, canbus);
		}

		/**
		 * Create a TalonMotor on the RoboRIO's canbus
		 * 
		 * @param canID [0, 62] the can id of the motor
		 */
		public TalonMotor(int canID) {
			super(checkCanId(canID));
			information = new DeviceInformation(canID);
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof TalonMotor))
				return false;
			TalonMotor other = (TalonMotor) o;
			return information.equals(other.information);
		}

		@Override
		public int hashCode() {
			return information.hashCode();
		}
	}

	/**
	 * Create a TalonFXWrapper on the specified canbus
	 * 
	 * @param canID      [0, 62] the can ID of the motor
	 * @param canbus     the canbus that the motor is on
	 * @param invertType the invert type
	 * @throws NullPointerException if either {@code invertType} or {@code canbus}
	 *                              are null
	 * 
	 */
	public TalonFXWrapper(int canID, String canbus, TalonFXInvertType invertType) {
		Objects.requireNonNull(invertType, "invertType should not be null");
		Objects.requireNonNull(canbus, "canbus should not be null");
		motor = new TalonFX(canID, canbus);

		TalonFXConfiguration config = new TalonFXConfiguration();
		config.voltageCompSaturation = 12;
		config.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 40, 40, 0.25);
		ConfigUtils.ctreConfig(() -> motor.configAllSettings(config));

		motor.enableVoltageCompensation(true);
		motor.setInverted(invertType);

		information = new DeviceInformation(canID, canbus);
	}

	/**
	 * Create a TalonFXWrapper on the RoboRIO's canbus
	 * 
	 * @param canID      [0, 62] the can ID of the motor
	 * @param invertType the invert type
	 * @throws NullPointerException if {@code invertType} is null
	 */
	public TalonFXWrapper(int canID, TalonFXInvertType invertType) {
		Objects.requireNonNull(invertType, "invertType should not be null");
		motor = new TalonFX(canID);

		TalonFXConfiguration config = new TalonFXConfiguration();
		config.voltageCompSaturation = 12;
		config.supplyCurrLimit = new SupplyCurrentLimitConfiguration(
				true,
				40,
				40,
				0.25);
		ConfigUtils.ctreConfig(() -> motor.configAllSettings(config));

		motor.enableVoltageCompensation(true);
		motor.setInverted(invertType);

		information = new DeviceInformation(canID);
	}

	public void set(ControlMode controlMode, double demand) {
		set(controlMode, demand, 0);
	}

	@Override
	public void set(ControlMode controlMode, double demand, double feedForward) {
		switch (controlMode) {
			case VELOCITY:
				demand = Units2813.motorRevsToTicks(demand / 60 / 10, 2048);
				break;
			case MOTION_MAGIC:
				demand = Units2813.motorRevsToTicks(demand, 2048);
				break;
			case DUTY_CYCLE:
				break;
			default:
				break;
		}
		motor.set(controlMode.getTalonMode(), demand, DemandType.ArbitraryFeedForward, feedForward);
	}

	@Override
	public double position() {
		return Units2813.ticksToMotorRevs(motor.getSelectedSensorPosition(), 2048);
	}

	@Override
	public void setPosition(double position) {
		motor.setSelectedSensorPosition(Units2813.motorRevsToTicks(position, 2048));
	}

	@Override
	public double getVelocity() {return Units2813.ticksToMotorRevs(motor.getSelectedSensorVelocity(), 2048);}

	public TalonFX motor() {
		return motor;
	}

	/**
	 * A list of followers, so that they aren't garbage collected
	 */
	private final List<TalonFX> followers = new ArrayList<>();
	/**
	 * the internal motor
	 */
	private final TalonFX motor;
	private final DeviceInformation information;

	@Override
	public void configPIDF(int slot, double p, double i, double d, double f) {
		ConfigUtils.ctreConfig(() -> motor.config_kP(slot, p));
		ConfigUtils.ctreConfig(() -> motor.config_kI(slot, i));
		ConfigUtils.ctreConfig(() -> motor.config_kD(slot, d));
		ConfigUtils.ctreConfig(() -> motor.config_kF(slot, f));
	}

	@Override
	public void configPIDF(double p, double i, double d, double f) {
		configPIDF(0, p, i, d, f);
	}

	@Override
	public void configPID(int slot, double p, double i, double d) {
		configPIDF(slot, p, i, d, 0);
	}

	@Override
	public void configPID(double p, double i, double d) {
		configPIDF(0, p, i, d, 0);
	}

	public void addFollower(int deviceNumber, String canbus, TalonFXInvertType invertType) {
		TalonFX follower = new TalonMotor(deviceNumber, canbus);
		follower.follow(motor);
		follower.setInverted(invertType);
		followers.add(follower); // add to follower list so TalonFX follower object is preserved
	}

	public void addFollower(int deviceNumber, String canbus, FollowerType followerType) {
		TalonFX follower = new TalonMotor(deviceNumber, canbus);
		follower.follow(motor, followerType);
		followers.add(follower); // add to follower list so TalonFX follower object is preserved
	}

	public void addFollower(int deviceNumber, TalonFXInvertType invertType) {
		TalonFX follower = new TalonMotor(deviceNumber);
		follower.follow(motor);
		follower.setInverted(invertType);
		followers.add(follower); // add to follower list so TalonFX follower object is preserved
	}

	public void addFollower(int deviceNumber, FollowerType followerType) {
		TalonFX follower = new TalonMotor(deviceNumber);
		follower.follow(motor, followerType);
		followers.add(follower); // add to follower list so TalonFX follower object is preserved
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TalonFXWrapper))
			return false;
		TalonFXWrapper other = (TalonFXWrapper) o;
		return information.equals(other.information) && followers.equals(other.followers);
	}

	@Override
	public int hashCode() {
		return information.hashCode() * 31 + followers.hashCode();
	}
}
