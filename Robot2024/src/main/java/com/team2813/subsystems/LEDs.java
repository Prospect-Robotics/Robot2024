package com.team2813.subsystems;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix.CANifier;
import com.team2813.lib2813.subsystems.lightshow.QueueLightshow;
import com.team2813.lib2813.subsystems.lightshow.State;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;
import static com.team2813.Constants.*;

public class LEDs extends QueueLightshow {
	final CANifier canifier;
	public LEDs() {
		super(States.class);
		canifier = new CANifier(CANIFIER);
		setDefaultState(States.OFF);
	}

	protected void useColor(Color c) {
		canifier.setLEDOutput(c.red, CANifier.LEDChannel.LEDChannelA);
		canifier.setLEDOutput(c.green, CANifier.LEDChannel.LEDChannelB);
		canifier.setLEDOutput(c.blue, CANifier.LEDChannel.LEDChannelC);
	}

	public enum States implements State {
		OFF(new Color(0, 0, 0), () -> true),
		BLUE(new Color(0, 0, 255), DriverStation::isEnabled);
		private final Color c;
		private final BooleanSupplier sup;
		States(Color c, BooleanSupplier sup) {
			this.c = c;
			this.sup = sup;
		}
		@Override
		public Color color() {
			return c;
		}
		@Override
		public boolean apply() {
			return sup.getAsBoolean();
		}
	}
}
