package com.Whodundid.core.util.renderUtil;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class HSLColor {
	
	double hue = 0;
	double saturation = 0;
	double luminance = 0;
	
	public HSLColor(double hueIn, double saturationIn, double luminanceIn) {
		hue = hueIn;
		saturation = saturationIn;
		luminance = luminanceIn;
	}
	
	public double getHue() { return hue; }
	public double getSaturation() { return saturation; }
	public double getLuminance() { return luminance; }
	public HSLColor setHue(double hueIn) { hue = hueIn; return this; }
	public HSLColor setSaturation(double saturationIn) { saturation = saturationIn; return this; }
	public HSLColor setLuminance(double luminanceIn) { luminance = luminanceIn; return this; }
	
	public int getRGB() {
		int r = 0;
		int g = 0;
		int b = 0;
		double rHolder = 0;
		double gHolder = 0;
		double bHolder = 0;
		double temp1 = 0, temp2 = 0;
		
		if (saturation == 0.00d) {
			int val = (int) round(luminance * 255, 2);
			r = val;
			g = val;
			b = val;
			String rC = Integer.toString(r, 16);
			String gC = Integer.toString(g, 16);
			String bC = Integer.toString(b, 16);
			return Integer.parseInt(rC + gC + bC, 16);
		}
		
		if (luminance < 0.50d) { temp1 = luminance * (1.00d + saturation); }
		else if (luminance >= 0.50d) { temp1 = luminance + saturation - luminance * saturation; }
		
		temp2 = 2.00d * luminance - temp1;
		
		double hueVal = (hue % 360) / 360;
		
		double tR = hueVal + 0.333;
		double tG = hueVal;
		double tB = hueVal - 0.333;
		
		if (tR < 0) { tR += 1.0d; } else if (tR > 1.0d) { tR -= 1.0d; }
		if (tG < 0) { tG += 1.0d; } else if (tG > 1.0d) { tG -= 1.0d; }
		if (tB < 0) { tB += 1.0d; } else if (tB > 1.0d) { tB -= 1.0d; }
		
		if (6 * tR < 1.0d) { rHolder = temp2 + (temp1 - temp2) * 6 * tR; }
		else {
			if (2 * tR < 1.0d) { rHolder = temp1; }
			else if (3 * tR < 2.0d) { rHolder = temp2 + (temp1 - temp2) * (0.666 - tR) * 6; }
			else { rHolder = temp2; }
		}
		
		if (6 * tG < 1.0d) { gHolder = temp2 + (temp1 - temp2) * 6 * tG; }
		else {
			if (2 * tG < 1.0d) { gHolder = temp1; }
			else if (3 * tG < 2.0d) { gHolder = temp2 + (temp1 - temp2) * (0.666 - tG) * 6; }
			else { gHolder = temp2; }
		}
		
		if (6 * tB < 1.0d) { bHolder = temp2 + (temp1 - temp2) * 6 * tB; }
		else {
			if (2 * tB < 1.0d) { bHolder = temp1; }
			else if (3 * tB < 2.0d) { bHolder = temp2 + (temp1 - temp2) * (0.666 - tB) * 6; }
			else { bHolder = temp2; }
		}
		
		r = (int) (rHolder * 255);
		g = (int) (gHolder * 255);
		b = (int) (bHolder * 255);
		
		String rC = Integer.toString(r, 16);
		String gC = Integer.toString(g, 16);
		String bC = Integer.toString(b, 16);
		return Integer.parseInt(rC + gC + bC, 16);
	}
	
	public static HSLColor convertFromRGB(Color colorIn) {
		if (colorIn != null) {
			return convertFromRGB(colorIn.getRGB());
		}
		return null;
	}
	
	public static HSLColor convertFromRGB(String colorIn) {
		if (!colorIn.isEmpty() && colorIn.length() == 6) {
			String rS = colorIn.substring(0, 2);
			String gS = colorIn.substring(2, 4);
			String bS = colorIn.substring(4, 6);
			int r = Integer.parseInt(rS, 16);
			int g = Integer.parseInt(gS, 16);
			int b = Integer.parseInt(bS, 16);
			return makeHSLColor(r, g, b);
		}
		return null;
	}
	
	public static HSLColor convertFromRGB(int colorIn) {
		int r = (colorIn >> 16) & 0xff;
		int g = (colorIn >> 8) & 0xff;
		int b = colorIn & 0xff;
		return makeHSLColor(r, g, b);
	}
	
	private static HSLColor makeHSLColor(int r, int g, int b) {
		double rPrime = round(r / 255, 2);
		double gPrime = round(g / 255, 2);
		double bPrime = round(b / 255, 2);
		
		double cMax = getMax(rPrime, gPrime, bPrime);
		double cMin = getMin(rPrime, gPrime, bPrime);
		
		double luminance = round((cMax + cMin) / 2, 2);
		double saturation = 0.00d;
		double hue = 0.00d;
		
		if (cMax != cMin) {
			if (luminance > 0.50d) { saturation = round(round(cMax - cMin, 2) / round(cMax + cMin, 2), 2); }
			else { saturation = round(round((cMax - cMin), 2) / (round(2.00d - cMax - cMin, 2)), 2); }
		}
		else { saturation = 0.00d; }
		
		if (cMax == rPrime) { hue = round(round(gPrime - bPrime, 2) / round(cMax - cMin, 2), 2); }
		else if (cMax == gPrime) { hue = round(2.00d + (round(bPrime - rPrime, 2) / round(cMax - cMin, 2)), 2); }
		else { hue = round(4.00d + (round(rPrime - gPrime, 2) / round(cMax - cMin, 2)), 2); }
		
		hue = round(hue * 60.00d, 2);
		if (hue < 0.00d) { hue = round(hue + 360.00d, 2); }
		
		return new HSLColor(hue, saturation, luminance);
	}
	
	private static double getMin(double a, double b, double c) {
		double min;
		if (a <= b && a <= c) { min = a; }
		else if (b <= c && b <= a) { min = b; }
		else { min = c; }
		return min;
	}
	
	private static double getMax(double a, double b, double c) {
		double max;
		if (a >= b && a >= c) { max = a; }
		else if (b >= c && b >= a) { max = b; }
		else { max = c; }
		return max;
	}
	
	private static double round(double value, int places) {
		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}
