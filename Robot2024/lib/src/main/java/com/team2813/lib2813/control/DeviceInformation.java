package com.team2813.lib2813.control;

public class DeviceInformation {
	private int id;
	private String canbus;
	public DeviceInformation(int id) {
		this.id = id;
		canbus = "";
	}
	public DeviceInformation(int id, String canbus) {
		this.id = id;
		this.canbus = canbus;
	}
	public int id() {
		return id;
	}
	public String canbus() {
		return canbus;
	}
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof DeviceInformation))
			return false;
		DeviceInformation other = (DeviceInformation) o;
		return other.id == id && other.canbus == canbus;
	}
	@Override
	public int hashCode() {
		return id * 31 + canbus.hashCode();
	}
}
