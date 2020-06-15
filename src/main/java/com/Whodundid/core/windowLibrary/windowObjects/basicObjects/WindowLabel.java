package com.Whodundid.core.windowLibrary.windowObjects.basicObjects;

import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowTypes.WindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class WindowLabel extends WindowObject {
	
	public String displayString = "";
	public int displayStringColor = 0xffffff;
	protected EArrayList<String> wordWrappedLines;
	protected int widthMax = 0;
	protected boolean wordWrap = false;
	protected boolean centered = false;
	protected boolean shadow = true;
	protected int gapSize = 0;
	
	public WindowLabel(IWindowObject parentIn, int xPos, int yPos) { this(parentIn, xPos, yPos, "", 0xffffff); }
	public WindowLabel(IWindowObject parentIn, int xPos, int yPos, String stringIn) { this(parentIn, xPos, yPos, stringIn, 0xffffff); }
	public WindowLabel(IWindowObject parentIn, int xPos, int yPos, String stringIn, EColors colorIn) { this(parentIn, xPos, yPos, stringIn, colorIn.c()); }
	public WindowLabel(IWindowObject parentIn, int xPos, int yPos, String stringIn, int colorIn) {
		init(parentIn, xPos, yPos, mc.fontRendererObj.getStringWidth(stringIn), mc.fontRendererObj.FONT_HEIGHT);
		displayString = stringIn;
		displayStringColor = colorIn;
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		if (wordWrap && wordWrappedLines != null) {
			int i = 0;
			for (String s : wordWrappedLines) {
				if (centered) {
					if (shadow) { drawCenteredStringWithShadow(s, startX, startY + (i * 9) + (i > 0 ? i * gapSize : 0), displayStringColor); }
					else { drawCenteredString(s, startX, startY + (i * 9) + (i > 0 ? i * gapSize : 0), displayStringColor); }
				}
				else {
					if (shadow) { drawStringWithShadow(s, startX, startY + (i * 9) + (i > 0 ? i * gapSize : 0), displayStringColor); }
					else { drawString(s, startX, startY + (i * 9) + (i > 0 ? i * gapSize : 0), displayStringColor); }
				}
				i++;
			}
		} else {
			if (centered) {
				if (shadow) { drawCenteredStringWithShadow(displayString, startX, startY, displayStringColor); }
				else { drawCenteredString(displayString, startX, startY, displayStringColor); }
			}
			else {
				if (shadow) { drawStringWithShadow(displayString, startX, startY, displayStringColor); }
				else { drawString(displayString, startX, startY, displayStringColor); }
			}
		}
		super.drawObject(mX, mY);
	}
	
	public WindowLabel setString(String stringIn) {
		displayString = stringIn;
		if (wordWrap) { wordWrappedLines = EUtil.createWordWrapString(displayString, widthMax); }
		setDimensions(mc.fontRendererObj.getStringWidth(displayString), mc.fontRendererObj.FONT_HEIGHT);
		return this;
	}
	
	public WindowLabel enableWordWrap(boolean val, int widthMaxIn) {
		boolean oldVal = wordWrap;
		widthMax = widthMaxIn;
		wordWrap = val;
		if (wordWrap && wordWrap != oldVal) { wordWrappedLines = EUtil.createWordWrapString(displayString, widthMax); }
		return this;
	}
	
	public int getTextHeight() { return wordWrap ? (wordWrappedLines.size() * 9 + wordWrappedLines.size() * gapSize) : mc.fontRendererObj.FONT_HEIGHT; }
	public String getString() { return displayString; }
	public int getColor() { return displayStringColor; }
	
	public boolean isEmpty() { return (displayString != null) ? displayString.isEmpty() : true; }
	
	public WindowLabel clear() { if (displayString != null) { displayString = ""; } return this; }
	public WindowLabel setLineGapHeight(int heightIn) { gapSize = heightIn; return this; }
	public WindowLabel setColor(int colorIn) { displayStringColor = colorIn; return this; }
	public WindowLabel setColor(EColors colorIn) { if (colorIn != null) { displayStringColor = colorIn.c(); } return this; }
	public WindowLabel enableShadow(boolean val) { shadow = val; return this; }
	public WindowLabel setDrawCentered(boolean val) { centered = val; return this; }
	
	public static void setColor(EColors colorIn, WindowLabel label, WindowLabel... additional) { setColor(colorIn.intVal, label, additional); }
	public static void setColor(int colorIn, WindowLabel label, WindowLabel... additional) {
		EUtil.filterNullDo(l -> l.setColor(colorIn), EUtil.add(label, additional));
	}
	
	public static void enableShadow(boolean val, WindowLabel label, WindowLabel... additional) {
		EUtil.filterNullDo(l -> l.enableShadow(val), EUtil.add(label, additional));
	}
	
}
