package com.team2813.subsystems;

import static com.team2813.Constants.CANIFIER;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import com.ctre.phoenix.CANifier;
import com.team2813.lib2813.subsystems.lightshow.Lightshow;
import com.team2813.lib2813.subsystems.lightshow.QueueLightshow;
import com.team2813.lib2813.subsystems.lightshow.State;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;

public class LEDs extends QueueLightshow {
	private final CANifier canifier;
	private final Magazine magazine;
	public LEDs(Magazine magazine) {
		super(new HashSet<>());
		canifier = new CANifier(CANIFIER);
		this.magazine = magazine;
		Set<State> states = new HashSet<>();
		for (NewStates s : NewStates.values()) {
			states.add(s.createState(this));
		}
		addStates(states);
	}

	protected void useColor(Color c) {
		canifier.setLEDOutput(c.red, CANifier.LEDChannel.LEDChannelB);
		canifier.setLEDOutput(c.green, CANifier.LEDChannel.LEDChannelA);
		canifier.setLEDOutput(c.blue, CANifier.LEDChannel.LEDChannelC);
	}

	private enum NewStates {
		Disabled(new Color(255, 0, 0), (j) -> true),
		Enabled(new Color(255, 255, 255), (j) -> DriverStation.isEnabled()),
		NoteInMag(
			new Color(255, 165, 0),
			(j) -> j.magazine.noteInMag() && DriverStation.isEnabled(), 0.25
		);
		
		private final Color c;
		private final Function<LEDs, Boolean> sup;
		private final double blinkTime;
		private double lastBlink = 0;
		private boolean blinkOn = false;
		NewStates(Color c, Function<LEDs, Boolean> sup) {
			this(c, sup, 0);
		}
		NewStates(Color c, Function<LEDs, Boolean> sup, double blinkTime) {
			this.c = c;
			this.sup = sup;
			this.blinkTime = blinkTime;
		}
		private State createState(LEDs leds) {
			return new State() {
				@Override
				public Color color() {
					if (blinkTime > 0) {
						double timestamp = Timer.getFPGATimestamp();
						if (timestamp - lastBlink > blinkTime) {
							lastBlink = timestamp;
							blinkOn = !blinkOn;
						}
						return blinkOn ? c : Lightshow.off.color();
					}
					return c;
				}

				@Override
				public boolean apply() {
					return sup.apply(leds);
				}
			};
		}
	}
}
