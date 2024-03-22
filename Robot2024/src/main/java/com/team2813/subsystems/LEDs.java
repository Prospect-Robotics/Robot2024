package com.team2813.subsystems;

import static com.team2813.Constants.CANIFIER;
import static com.team2813.Constants.OperatorConstants.intakeButton;

import java.util.function.Function;
import java.util.HashSet;
import java.util.Set;

import com.ctre.phoenix.CANifier;
import com.team2813.lib2813.subsystems.lightshow.QueueLightshow;
import com.team2813.lib2813.subsystems.lightshow.State;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;

public class LEDs extends QueueLightshow {
	private final CANifier canifier;
	private final Magazine magazine;
	private final Intake intake;
	public LEDs(Magazine magazine, Intake intake) {
		super(new HashSet<>());
		canifier = new CANifier(CANIFIER);
		this.magazine = magazine;
		this.intake = intake;
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
		MagStalled(
			new Color(255, 255, 0),
			(j) -> j.intake.isStalled()
		),
		Blue(new Color(0, 0, 255), (j) -> false),
		NoteInMag(new Color(255, 165, 0), (j) -> j.magazine.noteInMag() && DriverStation.isEnabled()),
		Enabled(new Color(255, 255, 255), (j) -> DriverStation.isEnabled());
		private final Color c;
		private final Function<LEDs, Boolean> sup;
		NewStates(Color c, Function<LEDs, Boolean> sup) {
			this.c = c;
			this.sup = sup;
		}
		private State createState(LEDs leds) {
			return new State() {
				@Override
				public Color color() {
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
