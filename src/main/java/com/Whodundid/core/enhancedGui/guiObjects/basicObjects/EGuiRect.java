package com.Whodundid.core.enhancedGui.guiObjects.basicObjects;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.EColors;

//Author: Hunter Bragg

public class EGuiRect extends EGuiShape {
	
	public EGuiRect(IEnhancedGuiObject parentIn, int startX, int startY, int endX, int endY) { this(parentIn, startX, startY, endX, endY, true, EColors.black); }
	public EGuiRect(IEnhancedGuiObject parentIn, int startX, int startY, int endX, int endY, EColors colorIn) { this(parentIn, startX, startY, endX, endY, true, colorIn.intVal); }
	public EGuiRect(IEnhancedGuiObject parentIn, int startX, int startY, int endX, int endY, int colorIn) { this(parentIn, startX, startY, endX, endY, true, colorIn); }
	public EGuiRect(IEnhancedGuiObject parentIn, int startX, int startY, int endX, int endY, boolean filledIn) { this(parentIn, startX, startY, endX, endY, filledIn, EColors.black.intVal); }
	public EGuiRect(IEnhancedGuiObject parentIn, int startX, int startY, int endX, int endY, boolean filledIn, EColors colorIn) { this(parentIn, startX, startY, endX, endY, filledIn, EColors.black.intVal); }
	public EGuiRect(IEnhancedGuiObject parentIn, int startX, int startY, int endX, int endY, boolean filledIn, int colorIn) {
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
	
	//----------------
	//EGuiRect Getters
	//----------------
	
	public int getColor() { return color; }

	//----------------
	//EGuiRect Setters
	//----------------
	
	public EGuiRect setColor(EColors colorIn) { return setColor(colorIn.c()); }
	public EGuiRect setColor(int colorIn) { color = colorIn; return this; }
	
}
