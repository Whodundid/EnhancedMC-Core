package com.Whodundid.core.enhancedGui.guiObjects.basicObjects;

import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.EColors;

public abstract class EGuiShape extends EnhancedGuiObject {
	
	boolean filled = false;
	int color = 0xffffffff;
	
	//-----------------
	//EGuiShape Getters
	//-----------------
	
	public boolean isFilled() { return filled; }
	public int getColor() { return color; }
	
	//-----------------
	//EGuiShape Setters
	//-----------------
	
	public EGuiShape setFilled(boolean val) { filled = val; return this; }
	public EGuiShape setColor(EColors colorIn) { return setColor(colorIn.intVal); }
	public EGuiShape setColor(int colorIn) { color = colorIn; return this; }

}
