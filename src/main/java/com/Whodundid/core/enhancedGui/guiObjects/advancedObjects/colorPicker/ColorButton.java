package com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.colorPicker;

import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.EColors;

//Author: Hunter Bragg

public class ColorButton extends EGuiButton {

	public ColorButton(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height) { this(parentIn, posX, posY, width, height, "White", EColors.white); }
	public ColorButton(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height, String textIn, EColors colorIn) { this(parentIn, posX, posY, width, height, textIn, colorIn.c()); }
	public ColorButton(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height, String textIn, int colorIn) {
		super(parentIn, posX, posY, width, height);
		setTextures(null, null);
		setDrawBackground(true);
		setParams(textIn, colorIn);
	}
	
	public ColorButton setParams(String textIn, EColors colorIn) { return setParams(textIn, colorIn.c()); }
	public ColorButton setParams(String textIn, int colorIn) { setColor(colorIn); setHover(textIn, 0xffffffff); return this; }
	
	public ColorButton setHover(String textIn, EColors colorIn) { return setHover(textIn, colorIn.c()); }
	public ColorButton setHover(String textIn, int colorIn) { setHoverText(textIn).setHoverTextColor(colorIn); return this; }
	
	public ColorButton setColor(EColors colorIn) { setBackgroundColor(colorIn); return this; }
	public ColorButton setColor(int colorIn) { setBackgroundColor(colorIn); return this; }
	
	public int getColor() { return getBackgroundColor(); }
}
