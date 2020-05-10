package com.Whodundid.core.util.renderUtil;

import java.awt.Color;

//Author: Hunter Bragg

public enum EColors {
	
	red(0xffff0000, "Red"),
	lred(0xffff5555, "Light Red"),
	orange(0xffffaa00, "Orange"),
	yellow(0xffffff00, "Yellow"),
	lime(0xffaaff00, "Lime"),
	green(0xff55ff55, "Light Green"),
	darkGreen(0xff00af00, "Green"),
	seafoam(0xff00ff8c, "Seafoam"),
	cyan(0xff00ffff, "Cyan"),
	blue(0xff0065ff, "Blue"),
	navy(0xff0000ff, "Navy"),
	purple(0xff6a0dad, "Purple"),
	magenta(0xffff00ff, "Magenta"),
	pink(0xfff872e9, "Pink"),
	white(0xffffffff, "White"),
	lgray(0xffb2b2b2, "Light Gray"),
	gray(0xff8d8d8d, "Gray"),
	dgray(0xff474747, "Dark Gray"),
	vdgray(0xff111111, "Very Dark Gray"),
	steel(0xff1f1f1f, "Steel"),
	black(0xff000000, "Black");
	
	public int intVal;
	public String name;
	
	EColors(int colorIn, String nameIn) {
		intVal = colorIn;
		name = nameIn;
	}

	/** Returns the color integer. */
	public int c() { return intVal; }
	/** Returns the color name. */
	public String n() { return name; }
	
	/** Returns an EColors with the corresponding integer color (if any). */
	public static EColors getEColor(int colorIn) {
		switch (colorIn) {
		case 0xffff0000: return red;
		case 0xffff5555: return lred;
		case 0xffffaa00: return orange;
		case 0xffffff00: return yellow;
		case 0xffaaff00: return lime;
		case 0xff55ff55: return green;
		case 0xff00af00: return darkGreen;
		case 0xff00ff8c: return seafoam;
		case 0xff00ffff: return cyan;
		case 0xff0065ff: return blue;
		case 0xff0000ff: return navy;
		case 0xff6a0dad: return purple;
		case 0xffff00ff: return magenta;
		case 0xfff872e9: return pink;
		case 0xffffffff: return white;
		case 0xffb2b2b2: return lgray;
		case 0xff8d8d8d: return gray;
		case 0xff474747: return dgray;
		case 0xff111111: return vdgray;
		case 0xff1f1f1f: return steel;
		case 0xff000000: return black;
		default: return null;
		}
	}
	
	/** Returns an EColors with the corresponding String name (if any). */
	public static EColors getEColor(String colorNameIn) {
		if (colorNameIn != null) {
			switch (colorNameIn.toLowerCase()) {
			case "red": return red;
			case "lred": case "lightred": case "light red": return lred;
			case "orange": return orange;
			case "yellow": return yellow;
			case "lime": return lime;
			case "lgreen": case "lightgreen": case "light green": return green;
			case "dgreen": case "darkgreen": case "dark green": return darkGreen;
			case "seafoam": return seafoam;
			case "cyan": return cyan;
			case "blue": return blue;
			case "navy": return navy;
			case "purple": return purple;
			case "magenta": return magenta;
			case "pink": return pink;
			case "white": return white;
			case "lgray": case "lightgray": case "light gray": return lgray;
			case "gray": return gray;
			case "dgray": case "darkgray": case "dark gray": return dgray;
			case "vdgray": case "verydarkgray": case "very dark gray": return vdgray;
			case "steel": return steel;
			case "black": return lred;
			}
		}
		return null;
	}
	
	/** Needs to be consistently called in order for any color change to occur. */
	public static int rainbow() {
		return Color.HSBtoRGB(System.currentTimeMillis() % 10000L / 10000.0f, 0.8f, 1f);
	}
}
