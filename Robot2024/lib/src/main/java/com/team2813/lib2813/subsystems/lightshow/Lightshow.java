package com.team2813.lib2813.subsystems.lightshow;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class Lightshow extends SubsystemBase {
	/**
	 * A static state that signifies that the lights should be off, that is always applied
	 */
	protected static final State off = new State() {
		public Color color() {
			return new Color(0, 0, 0);
		};
		public boolean apply() {
			return true;
		};
	};
	protected Set<State> states = new HashSet<>();
	protected Optional<State> defaultState;
	protected Consumer<Color> colorConsumer;

	/**
	 * Creates a new Lightshow subsystem. Uses the given {@code enumClass} to get a
	 * list of {@link State}s to use.
	 * 
	 * @param <T> The type of an enum that implements state
	 * @param enumClass
	 * @param colorConsumer
	 */
	protected <T extends Enum<T> & State> Lightshow(Class<T> enumClass, Consumer<Color> colorConsumer) {
		addStates(enumClass);
	}

	protected Lightshow(Set<State> states, Consumer<Color> colorConsumer) {
		addStates(states);
	}

	public final <T extends Enum<T> & State> void addStates(Class<T> enumClass) {
		T[] constants = enumClass.getEnumConstants();
		states.addAll(Arrays.asList(constants));
		if (defaultState.isEmpty()) {
			resolveDefault(enumClass);
		}
	}

	public final void addStates(Set<State> states) {
		this.states.addAll(states);
	}

	/**
	 * Resolves the default color from an enum class
	 * 
	 * @param <T>       the enum type to get from
	 * @param enumClass the class of the enum to get from
	 * @return an optional describing the default state, or an empty optional if no
	 *         default state was found
	 */
	protected final <T extends Enum<T> & State> Optional<State> resolveDefault(Class<T> enumClass) {
		try {
			for (T constant : enumClass.getEnumConstants()) {
				if (enumClass.getField(constant.name()).getAnnotation(Default.class) != null) {
					return Optional.of(constant);
				}
			}
		} catch (NoSuchFieldException e) {
			return resolveObfuscatedDefault(enumClass);
		}
		return Optional.empty();
	}

	/**
	 * Resolves the default state from an obfuscated enum. Returns the first enum
	 * constant it finds that has the
	 * {@link Default} annotation on it.
	 * 
	 * @param <T>       the type of Enum that is a State
	 * @param enumClass the class of the enum
	 * @return an optional representing the color
	 */
	protected final <T extends Enum<T> & State> Optional<State> resolveObfuscatedDefault(Class<T> enumClass) {
		List<T> constants = Arrays.asList(enumClass.getEnumConstants());
		for (Field field : enumClass.getDeclaredFields()) {
			if (field.isEnumConstant()) {
				try {
					Object resolved = field.get(null);
					if (enumClass.isInstance(resolved)
							&& constants.contains(resolved)
							&& field.getAnnotation(Default.class) != null) {
						return Optional.of(enumClass.cast(resolved));
					}
				} catch (IllegalAccessException e) {
					DriverStation.reportWarning("IllegalAccessException in Lightshow: " + e.getMessage(), false);
				}
			}
		}
		return Optional.empty();
	}

	/**
	 * Updates the current States.
	 * Makes the current {@link #states} updated,
	 * and gets the Color to be displayed.
	 * 
	 * @implSpec Do anything with {@link #states} to determine which {@link Color}
	 *           to display
	 * 
	 * @return an optional representing the color to be displayed
	 * @see #periodic()
	 */
	protected abstract Optional<Color> update();

	/**
	 * {@inheritDoc}
	 * <p>This implementation of {@link Subsystem#periodic()}
	 * updates the colors by passing the call of {@link #update()}
	 * to the given {@link Consumer}.
	 * If the call to {@link #update()} returns an empty optional,
	 * than the color of the default state is used, or the color is not changed.
	 * 
	 * @implSpec
	 *           The {@link #colorConsumer} is passed the result of
	 *           {@link #update()}, or the {@link #defaultState}'s color. If there
	 *           is no color from {@link #update()} and no default state, there is
	 *           no requirement for what to pass to the {@link #colorConsumer}, or
	 *           if the {@link #colorConsumer} is called at all
	 */
	@Override
	public void periodic() {
		Optional<Color> color = update().or(() -> defaultState.map(State::color));
		if (color.isPresent()) {
			colorConsumer.accept(color.get());
		}
	}
	
	public void setDefaultState(State defaultState) {
		this.defaultState = Optional.of(defaultState);
	}

	/**
	 * Marks an enum constant as being the default state.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface Default {

	}
}
