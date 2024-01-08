package com.team2813.lib2813.util;

public class Units2813 {
	private Units2813() {
		throw new AssertionError("non-instantiable");
	}
	public static double ticksToMotorRevs(double ticks, int cpr) {
        return ticks / cpr;
    }

    public static int motorRevsToTicks(double revs, int cpr) {
        return (int) (revs * cpr);
    }

    public static double motorRevsToWheelRevs(double revs, double gearRatio) {
        return revs * gearRatio;
    }

    public static double wheelRevsToMotorRevs(double revs, double gearRatio) {
        return revs / gearRatio;
    }
}
