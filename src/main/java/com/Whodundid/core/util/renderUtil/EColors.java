package com.Whodundid.core.util.renderUtil;

public enum EColors {
	
	white(0xffffffff),
	green(0xff55ff55),
	red(0xffff5555),
	yellow(0xffffff00),
	orange(0xffaa00),
	cyan(0xff00ffff),
	magenta(0xffff00dd),
	lgray(0xffb2b2b2),
	gray(0xff2d2d2d),
	dgray(0xff1f1f1f);
	
	private int color = 0xffffff;
	
	EColors(int colorIn) {
		color = colorIn;
	}

	public int c() { return color; }
}
