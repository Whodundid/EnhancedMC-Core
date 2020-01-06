package com.Whodundid.core.util.mathUtil;

import java.awt.Color;

//Author: Hunter Bragg

public class HexMath {
	
	public static int RGBtoHEX(Color color) {
        String hex = Integer.toHexString(color.getRGB() & 0xffffff);
        if (hex.length() < 6) {
            if (hex.length() == 5) { hex = "0" + hex; }
            if (hex.length() == 4) { hex = "00" + hex; }
            if (hex.length() == 3) { hex = "000" + hex; }
        }
        hex = "#" + hex;
        return Integer.decode(hex);
    }
	
	/** converts a floating point value between 0.0 and 1.0 to a corresponding hexadecimal value. */
	public static int floatToHex(float valIn) {
		int val = (int) (valIn * 255);
		String sVal = Integer.toHexString(val);
		System.out.println(sVal);
		return val;
	}
}
