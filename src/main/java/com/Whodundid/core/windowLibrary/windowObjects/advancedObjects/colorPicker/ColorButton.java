package com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.colorPicker;

import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class ColorButton extends WindowButton {

	public ColorButton(IWindowObject parentIn, int posX, int posY, int width, int height) { this(parentIn, posX, posY, width, height, "White", EColors.white); }
	public ColorButton(IWindowObject parentIn, int posX, int posY, int width, int height, EColors colorIn) { this(parentIn, posX, posY, width, height, colorIn.name, colorIn.intVal); }
	public ColorButton(IWindowObject parentIn, int posX, int posY, int width, int height, String textIn, EColors colorIn) { this(parentIn, posX, posY, width, height, textIn, colorIn.c()); }
	public ColorButton(IWindowObject parentIn, int posX, int posY, int width, int height, String textIn, int colorIn) {
		super(parentIn, posX, posY, width, height);
		setTextures(null, null);
		setDrawBackground(true);
		setParams(textIn, colorIn);
	}
	
	@Override
	public void onDoubleClick() {
		performAction("dc");
	}
	
	public ColorButton setParams(String textIn, EColors colorIn) { return setParams(textIn, colorIn.c()); }
	public ColorButton setParams(String textIn, int colorIn) { setColor(colorIn); setHover(textIn, EColors.seafoam); return this; }
	
	public ColorButton setHover(String textIn, EColors colorIn) { return setHover(textIn, colorIn.c()); }
	public ColorButton setHover(String textIn, int colorIn) { setHoverText(textIn).setHoverTextColor(colorIn); return this; }
	
	public ColorButton setColor(EColors colorIn) { setBackgroundColor(colorIn); return this; }
	public ColorButton setColor(int colorIn) { setBackgroundColor(colorIn); return this; }
	
	public int getColor() { return getBackgroundColor(); }
	
}
