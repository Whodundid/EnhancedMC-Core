package com.Whodundid.slc.util;

public enum GlobalModes {
	SW("switch"),
	BL("blink"),
	IN("individual"),
	OFF("off");
	
	private final String type;
	
	GlobalModes(String type) {
		this.type = type;
	}
	
	public String getModeType() { return type; }
}