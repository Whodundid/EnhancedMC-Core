package com.Whodundid.core.windowLibrary.windowObjects.utilityObjects;

import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

public class ColorButton extends WindowButton {
	
	private int color = 0xffffffff;
	private boolean drawBorder = true;
	
	//------------------------
	//ColorButton Constructors
	//------------------------
	
	public ColorButton(IWindowObject parent, int xIn, int yIn, int widthIn, int heightIn) { this(parent, xIn, yIn, widthIn, heightIn, 0xffffffff); }
	public ColorButton(IWindowObject parent, int xIn, int yIn, int widthIn, int heightIn, int colorIn) {
		super(parent, xIn, yIn, widthIn, heightIn);
		color = colorIn;
		setDrawTextures(false);
	}
	
	//----------------------
	//WindowButton Overrides
	//----------------------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
		
		if (drawBorder) {
			drawRect(EColors.black);
			drawRect(color, 1);
		}
		else {
			drawRect(color);
		}
	}
	
	//-------------------
	//ColorButton Getters
	//-------------------
	
	public int getColor() { return color; }
	
	//-------------------
	//ColorButton Setters
	//-------------------
	
	public ColorButton setColor(EColors colorIn) { if (colorIn != null) { color = colorIn.intVal; } return this; }
	public ColorButton setColor(int colorIn) { color = colorIn; return this; }
	public ColorButton setDrawBorder(boolean val) { drawBorder = val; return this; }
	
}