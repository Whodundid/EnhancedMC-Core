package com.Whodundid.core.windowLibrary.windowObjects.basicObjects;

import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.windowLibrary.windowTypes.WindowObject;

public abstract class WindowShape extends WindowObject {
	
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
	
	public WindowShape setFilled(boolean val) { filled = val; return this; }
	public WindowShape setColor(EColors colorIn) { return setColor(colorIn.intVal); }
	public WindowShape setColor(int colorIn) { color = colorIn; return this; }

}
