package com.Whodundid.core.enhancedGui.guiObjects.basicObjects;

import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.EColors;

public class EGuiLine extends EnhancedGuiObject {
	
	int x1, y1, x2, y2;
	int color;
	
	public EGuiLine(IEnhancedGuiObject parentIn, int x1, int y1, int x2, int y2) { this(parentIn, x1, y1, x2, y2, 0xff000000); }
	public EGuiLine(IEnhancedGuiObject parentIn, int x1, int y1, int x2, int y2, EColors colorIn) { this(parentIn, x1, y1, x2, y2, colorIn.intVal); }
	public EGuiLine(IEnhancedGuiObject parentIn, int x1, int y1, int x2, int y2, int colorIn) {
		
		if (x1 > x2) {
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}
		if (y1 > y2) {
			int temp = y1;
			y1 = y2;
			y2 = temp;
		}
		
		
		int w = x2 - x1;
		int h = y2 - y1;
		
		color = colorIn;
		
		init(parentIn, x1, y1, w, h);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawLine(startX, startY, endX, endY, color);
		
		super.drawObject(mXIn, mYIn, ticks);
	}
	
}
