package com.Whodundid.core.util.renderUtil;

import java.awt.Color;

//Author: Hunter Bragg

public enum EColors {
	
	lred(0xffff5555, "Light Red"),
	red(0xffff0000, "Red"),
	maroon(0xff9e0012, "Maroon"),
	brown(0xff7F3000, "Brown"),
	dorange(0xffB24400, "Dark Orange"),
	borange(0xffff6600, "Bright Orange"),
	orange(0xffff9900, "Orange"),
	lorange(0xffffaa00, "Light Orange"),
	yellow(0xffffff00, "Yellow"),
	lime(0xffaaff00, "Lime"),
	green(0xff55ff55, "Light Green"),
	seafoam(0xff00ff8c, "Seafoam"),
	lgreen(0xff00ff00, "Green"),
	dgreen(0xff00af00, "Dark Green"),
	turquoise(0xff00c1ae, "Turquoise"),
	cyan(0xff00ffff, "Cyan"),
	blue(0xff0065ff, "Blue"),
	regal(0xff004EC4, "Regal Blue"),
	navy(0xff0000ff, "Navy"),
	grape(0xff4800ff, "Grape"),
	violet(0xff430093, "Violet"),
	eggplant(0xff772789, "Eggplant"),
	purple(0xffdd49ff, "Purple"),
	pink(0xfff872e9, "Pink"),
	hotpink(0xffff00dc, "Hot Pink"),
	magenta(0xffff0061, "Magenta"),
	white(0xffffffff, "White"),
	lgray(0xffb2b2b2, "Light Gray"),
	gray(0xff8d8d8d, "Gray"),
	dgray(0xff474747, "Dark Gray"),
	pdgray(0xff303030, "Pretty Dark Gray"),
	steel(0xff1f1f1f, "Steel"),
	dsteel(0xff191919, "Dark Steel"),
	vdgray(0xff111111, "Very Dark Gray"),
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
		case 0xffff5555: return lred;
		case 0xffff0000: return red;
		case 0xff9e0012: return maroon;
		case 0xff7f3000: return brown;
		case 0xffb24400: return dorange;
		case 0xffff6600: return borange;
		case 0xffff9900: return orange;
		case 0xffffaa00: return lorange;
		case 0xffffff00: return yellow;
		case 0xffaaff00: return lime;
		case 0xff55ff55: return green;
		case 0xff00ff8c: return seafoam;
		case 0xff00ff00: return lgreen;
		case 0xff00af00: return dgreen;
		case 0xff00c1ae: return turquoise;
		case 0xff00ffff: return cyan;
		case 0xff0065ff: return blue;
		case 0xff004EC4: return regal;
		case 0xff0000ff: return navy;
		case 0xff4800ff: return grape;
		case 0xff430093: return violet;
		case 0xff772789: return eggplant;
		case 0xffdd49ff: return purple;
		case 0xfff872e9: return pink;
		case 0xffff00dc: return hotpink;
		case 0xffff0061: return magenta;
		case 0xffffffff: return white;
		case 0xffb2b2b2: return lgray;
		case 0xff8d8d8d: return gray;
		case 0xff474747: return dgray;
		case 0xff303030: return pdgray;
		case 0xff1f1f1f: return steel;
		case 0xff111111: return vdgray;
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
			case "green": return lgreen;
			case "dgreen": case "darkgreen": case "dark green": return dgreen;
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
			case "pdgray": return pdgray;
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
