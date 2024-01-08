package com.team2813.lib2813.subsystems.lightshow;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import edu.wpi.first.wpilibj.util.Color;

/**
 * A lightshow that keeps track of the states that have been applied,
 * and uses the last state. To be more specific, all states that return {@code true} on a call to {@link State#apply()}
 * are added to the list. States are only removed from the list if they are at the front and {@link State#apply()} returns {@code false},
 * when a state is removed, the next one will be activated if {@link State#apply()} returns {@code true}, until either a state
 * returns {@code true} upon a call to {@link State#apply()}, in which the color will be used, or there are no states where
 * {@link State#apply()} return {@code true}, then the default color is used.
 */
public class QueueLightshow extends Lightshow {
	private final Deque<State> activatedStates = new ArrayDeque<>();
	public <T extends Enum<T> & State> QueueLightshow(Class<T> enumClass, Consumer<Color> colorConsumer) {
		super(enumClass, colorConsumer);
		defaultState = Optional.of(Lightshow.off);
	}

	public QueueLightshow(Set<State> states, Consumer<Color> colorConsumer) {
		super(states, colorConsumer);
		defaultState = Optional.of(Lightshow.off);
	}

	@Override
	protected Optional<Color> update() {
		for (State s : states) {
			if (!activatedStates.contains(s) && s.apply()) {
				activatedStates.addFirst(s);
			}
		}
		while (!activatedStates.isEmpty()) {
			State s = activatedStates.poll();
			if (s.apply()) {
				activatedStates.addFirst(s);
				return Optional.of(s.color());
			}
		}
		return Optional.empty();
	}
}
