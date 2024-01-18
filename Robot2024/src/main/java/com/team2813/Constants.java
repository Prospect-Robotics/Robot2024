// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team2813;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;

    //Mechanism CAN IDs
    public static final int INTAKE = 14;
    public static final int LEFT_MAGAZINE = 15;
    public static final int SHOOTER = 16;
    public static final int SHOOTER_PIVOT= 17;
    public static final int CLIMBER = 18;
    public static final int RIGHT_MAGAZINE = 19;
    public static final int INTAKE_PIVOT = 20;
  }
}
