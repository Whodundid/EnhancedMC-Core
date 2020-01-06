package com.Whodundid.core.util.renderUtil;

//Author: Hunter Bragg

/** A enum representing a location or direction on the physical screen. */
public enum ScreenLocation {
	top, topRight, right, botRight, bot, botLeft, left, topLeft, center, custom, out;
	
	public static boolean isCorner(ScreenLocation locIn) {
		switch (locIn) {
		case topRight:
		case botRight:
		case topLeft:
		case botLeft: return true;
		default: return false;
		}
	}
	
	public static boolean isSide(ScreenLocation locIn) {
		switch (locIn) {
		case top:
		case left:
		case bot:
		case right: return true;
		default: return false;
		}
	}
}