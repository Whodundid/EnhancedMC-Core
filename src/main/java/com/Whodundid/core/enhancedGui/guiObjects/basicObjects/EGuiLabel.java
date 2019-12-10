package com.Whodundid.core.enhancedGui.guiObjects.basicObjects;

import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Last edited: Jan 2, 2019
//First Added: Dec 18, 2018
//Author: Hunter Bragg

public class EGuiLabel extends EnhancedGuiObject {
	
	public String displayString = "";
	public int displayStringColor = 0xffffff;
	protected EArrayList<String> wordWrappedLines;
	protected int widthMax = 0;
	protected boolean wordWrap = false;
	protected boolean centered = false;
	protected boolean shadow = true;
	protected int gapSize = 0;
	
	public EGuiLabel(IEnhancedGuiObject parentIn, int xPos, int yPos, String stringIn) { this(parentIn, xPos, yPos, stringIn, 0xffffff); }
	public EGuiLabel(IEnhancedGuiObject parentIn, int xPos, int yPos, String stringIn, int colorIn) {
		init(parentIn, xPos, yPos, fontRenderer.getStringWidth(stringIn), fontRenderer.FONT_HEIGHT);
		displayString = stringIn;
		displayStringColor = colorIn;
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		if (wordWrap && wordWrappedLines != null) {
			int i = 0;
			for (String s : wordWrappedLines) {
				if (centered) {
					if (shadow) { drawCenteredStringWithShadow(s, startX, startY + (i * 9) + (i > 0 ? i * gapSize : 0), displayStringColor); }
					else { drawCenteredString(s, startX, startY + (i * 9) + (i > 0 ? i * gapSize : 0), displayStringColor); }
				} else {
					if (shadow) { drawStringWithShadow(s, startX, startY + (i * 9) + (i > 0 ? i * gapSize : 0), displayStringColor); }
					else { drawString(s, startX, startY + (i * 9) + (i > 0 ? i * gapSize : 0), displayStringColor); }
				}
				i++;
			}
		} else {
			if (centered) {
				if (shadow) { drawCenteredStringWithShadow(displayString, startX, startY, displayStringColor); }
				else { drawCenteredString(displayString, startX, startY, displayStringColor); }
			} else {
				if (shadow) { drawStringWithShadow(displayString, startX, startY, displayStringColor); }
				else { drawString(displayString, startX, startY, displayStringColor); }
			}
		}
		super.drawObject(mX, mY, ticks);
	}
	
	public EGuiLabel setDisplayString(String stringIn) {
		displayString = stringIn;
		if (wordWrap) { wordWrappedLines = EUtil.createWordWrapString(displayString, widthMax); }
		return this;
	}
	
	public EGuiLabel enableWordWrap(boolean val, int widthMaxIn) {
		boolean oldVal = wordWrap;
		widthMax = widthMaxIn;
		wordWrap = val;
		if (wordWrap && wordWrap != oldVal) { wordWrappedLines = EUtil.createWordWrapString(displayString, widthMax); }
		return this;
	}
	
	public EGuiLabel setLineGapHeight(int heightIn) { gapSize = heightIn; return this; }
	public EGuiLabel setDisplayStringColor(int colorIn) { displayStringColor = colorIn; return this; }
	public EGuiLabel enableShadow(boolean val) { shadow = val; return this; }
	public EGuiLabel setDrawCentered(boolean val) { centered = val; return this; }
	
	public int getTextHeight() { return wordWrap ? (wordWrappedLines.size() * 9 + wordWrappedLines.size() * gapSize) : fontRenderer.FONT_HEIGHT; }
	public String getDisplayString() { return displayString; }
	public int getDisplayStringColor() { return displayStringColor; }
}
