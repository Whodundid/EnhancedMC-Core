package com.Whodundid.core.windowLibrary.windowObjects.basicObjects;

import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class WindowRect extends WindowShape {
	
	public WindowRect(IWindowObject parentIn, int startX, int startY, int endX, int endY) { this(parentIn, startX, startY, endX, endY, true, EColors.black); }
	public WindowRect(IWindowObject parentIn, int startX, int startY, int endX, int endY, EColors colorIn) { this(parentIn, startX, startY, endX, endY, true, colorIn.intVal); }
	public WindowRect(IWindowObject parentIn, int startX, int startY, int endX, int endY, int colorIn) { this(parentIn, startX, startY, endX, endY, true, colorIn); }
	public WindowRect(IWindowObject parentIn, int startX, int startY, int endX, int endY, boolean filledIn) { this(parentIn, startX, startY, endX, endY, filledIn, EColors.black.intVal); }
	public WindowRect(IWindowObject parentIn, int startX, int startY, int endX, int endY, boolean filledIn, EColors colorIn) { this(parentIn, startX, startY, endX, endY, filledIn, EColors.black.intVal); }
	public WindowRect(IWindowObject parentIn, int startX, int startY, int endX, int endY, boolean filledIn, int colorIn) {
		init(parentIn, startX, startY, Math.abs(endX - startX), Math.abs(endY - startY));
		filled = filledIn;
		color = colorIn;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
		if (filled) { drawRect(startX, startY, endX, endY, color); }
		else { drawHRect(startX, startY, endX, endY, 1, color); }
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public WindowRect setDimensions(int newWidth, int newHeight) {
		width = Math.abs(newWidth);
		height = Math.abs(newHeight);
		if (newWidth < 0) { startX += newWidth; }
		if (newHeight < 0) { startY += newHeight; }
		endX = startX + width;
		endY = startY + height;
		midX = startX + (width / 2);
		midY = startY + (height / 2);
		return this;
	}
	
	//----------------
	//EGuiRect Getters
	//----------------
	
	public int getColor() { return color; }

	//----------------
	//EGuiRect Setters
	//----------------
	
	public WindowRect setColor(EColors colorIn) { return setColor(colorIn.c()); }
	public WindowRect setColor(int colorIn) { color = colorIn; return this; }
	
}
