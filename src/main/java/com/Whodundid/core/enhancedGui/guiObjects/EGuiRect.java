package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.enhancedGui.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;

public class EGuiRect extends EnhancedGuiObject {
	
	public int color = 0xff000000;
	
	public EGuiRect(IEnhancedGuiObject parentIn, int startX, int startY, int endX, int endY, int colorIn) {
		init(parentIn, startX, startY, Math.abs(endX - startX), Math.abs(endY - startY));
		color = colorIn;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawRect(startX, startY, endX, endY, color);
	}

	public EGuiRect setColor(int colorIn) { color = colorIn; return this; }
	public int getColor() { return color; }
}
