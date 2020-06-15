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
	aquamarine(0xff00ffdc, "Aquamarine"),
	cyan(0xff00ffff, "Cyan"),
	skyblue(0xff00baff, "Sky Blue"),
	blue(0xff0065ff, "Blue"),
	regal(0xff004EC4, "Regal Blue"),
	navy(0xff0000ff, "Navy"),
	grape(0xff4200ff, "Grape"),
	violet(0xff430093, "Violet"),
	eggplant(0xff772789, "Eggplant"),
	purple(0xffdd49ff, "Purple"),
	pink(0xfff872e9, "Pink"),
	hotpink(0xffff00dc, "Hot Pink"),
	magenta(0xffff0061, "Magenta"),
	white(0xffffffff, "White"),
	lgray(0xffb2b2b2, "Light Gray"),
	gray(0xff8d8d8d, "Gray"),
	mgray(0xff636363, "Medium Gray"),
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
		for (EColors c : values()) {
			if (c.intVal == colorIn) { return c; }
		}
		return null;
	}
	
	/** Returns an EColors with the corresponding String name (if any). */
	public static EColors getEColor(String colorNameIn) {
		if (colorNameIn != null) {
			for (EColors c : values()) {
				if (c.name.toLowerCase().equals(colorNameIn.toLowerCase())) { return c; }
			}
		}
		return null;
	}
	
	/** Needs to be consistently called in order for any color change to occur. */
	public static int rainbow() {
		return Color.HSBtoRGB(System.currentTimeMillis() % 10000L / 10000.0f, 0.8f, 1f);
	}
	
}
