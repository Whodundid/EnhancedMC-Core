package com.Whodundid.core.util.playerUtil;

//Author: Hunter Bragg

public enum Direction {
	N("N", 180f, "-Z"),
	NE("NE", 225f, "-Z / +X"),
	E("E", 270f, "+X"),
	SE("SE", 315f, "+Z / +X"),
	S("S", 0f, "+Z"),
	SW("SW", 45f, "+Z / -X"),
	W("W", 90f, "-X"),
	NW("NW", 135f, "-Z / -X"),
	OUT("Unknown", 0f, "Unknown");
	
	private String direction = "";
	private float degreeDir = 0;
	private String xzFacing = "";
	
	Direction(String dir, float degDir, String xz) {
		direction = dir;
		degreeDir = degDir;
		xzFacing = xz;
	}
	
	public String getDir() { return direction; }
	public float getDegree() { return degreeDir; }
	public String getXZFacing() { return xzFacing; }
	
}
