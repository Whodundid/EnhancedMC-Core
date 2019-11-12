package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

//Last edited: Jan 2, 2019
//First Added: Dec 30, 2018
//Author: Hunter Bragg

public class EGuiContainer extends EnhancedGuiObject {
	
	public String displayString = "";
	//public int displayStringColor = 0x47ff96;
	public int displayStringColor = 0xffffff;
	public int titleBorderColor = 0xff000000;
	public int titleBackgroundColor = 0xff222222;
	public int borderColor = 0xff000000;
	public int backgroundColor = 0xff4c4c4c;
	protected boolean drawTitle = true;
	protected boolean drawBackground = true;
	protected boolean drawBorder = true;
	protected boolean useCustomWidth = false;
	protected boolean drawTitleFullWidth = true;
	protected int displayStringWidth = 0;
	protected int titleWidth = 0;
	protected int titleHeight = 0;
	
	public EGuiContainer(IEnhancedGuiObject parentIn, int xIn, int yIn, int widthIn, int heightIn) {
		this(parentIn, xIn, yIn, widthIn, heightIn, true);
	}
	
	public EGuiContainer(IEnhancedGuiObject parentIn, int xIn, int yIn, int widthIn, int heightIn, boolean drawTitleIn) {
		init(parentIn, xIn, yIn, widthIn, heightIn);
		drawTitle = drawTitleIn;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		if (drawBorder) { drawRect(startX, startY, endX, endY, borderColor); } //border
		if (drawBackground) { drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor); } //inner
		if (drawTitle) {
			titleHeight = height >= 18 ? 18 : height;
			int drawWidth = displayStringWidth + 6;
			if (useCustomWidth) { drawWidth = titleWidth; }
			if (drawTitleFullWidth) { drawWidth = width - 1; }
			drawRect(startX + 1, startY + 1, startX + drawWidth + 1, startY + titleHeight, titleBorderColor);
			drawRect(startX + 1, startY + 1, startX + drawWidth, startY + titleHeight - 1, titleBackgroundColor);
			drawStringWithShadow(displayString, startX + 4, startY + 5, displayStringColor);
		}
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	public EGuiContainer setDisplayString(String stringIn) { displayString = stringIn; displayStringWidth = fontRenderer.getStringWidth(stringIn); return this; }
	public EGuiContainer setDisplayStringColor(int colorIn) { displayStringColor = colorIn; return this; }
	public EGuiContainer setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public EGuiContainer setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public EGuiContainer setTitleBorderColor(int colorIn) { titleBorderColor = colorIn; return this; }
	public EGuiContainer setTitleBackgroundColor(int colorIn) { titleBackgroundColor = colorIn; return this; }
	public EGuiContainer setDrawTitle(boolean val) { drawTitle = val; return this; }
	public EGuiContainer setDrawBackground(boolean val) { drawBackground = val; return this; }
	public EGuiContainer setDrawBorder(boolean val) { drawBorder = val; return this; }
	public EGuiContainer setTitleWidth(int widthIn) { if (widthIn > 0) { useCustomWidth = true; titleWidth = widthIn; } return this; }
	public EGuiContainer setTitleFullWidth(boolean val) { drawTitleFullWidth = val; return this; }
	public int getDisplayStringWidth() { return displayStringWidth; }
	public int getTitleHeight() { return titleHeight; }
}
