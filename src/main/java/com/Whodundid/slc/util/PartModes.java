package com.Whodundid.slc.util;

public enum PartModes {
	
	SW("switching"),
	BL("blinking"),
	IN("individual"),
	N("none");
	
	private String currentMode;
	
	PartModes(String mode) {
		currentMode = mode;
	}
	
	public String getMode() { return currentMode; }
}