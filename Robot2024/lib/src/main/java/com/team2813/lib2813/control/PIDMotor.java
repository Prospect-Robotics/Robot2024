package com.team2813.lib2813.control;

public interface PIDMotor extends Motor, Encoder {
	public double getVelocity();
	public void configPIDF(int slot, double p, double i, double d, double f);
	public void configPIDF(double p, double i, double d, double f);
	public void configPID(int slot, double p, double i, double d);
	public void configPID(double p, double i, double d);
}
