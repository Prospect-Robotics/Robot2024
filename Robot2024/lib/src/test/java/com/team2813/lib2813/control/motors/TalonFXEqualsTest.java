package com.team2813.lib2813.control.motors;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class TalonFXEqualsTest {
	@Test
	public void TalonEquals() {
		TalonFX a = new TalonFXWrapper.TalonMotor(1);
		TalonFX b = new TalonFXWrapper.TalonMotor(1);
		assertTrue(a.equals(b));
	}
	@Test
	public void IdentityTest() {
		TalonFXWrapper motor = new TalonFXWrapper(0, TalonFXInvertType.Clockwise);
		assertTrue(motor.equals(motor));
	}
}
