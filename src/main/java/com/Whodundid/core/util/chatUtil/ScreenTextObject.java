package com.Whodundid.core.util.chatUtil;

import com.Whodundid.core.util.renderUtil.EColors;

public class ScreenTextObject {

	private String text;
	private int xPos, yPos;
	private int color;
	private boolean center, shadow;
	
	//-----------------------------
	//ScreenTextObject Constructors
	//-----------------------------
	
	public ScreenTextObject(String textIn, int xIn, int yIn) { this(textIn, xIn, yIn, EColors.white.intVal, false, false); }
	public ScreenTextObject(String textIn, int xIn, int yIn, EColors colorIn) { this(textIn, xIn, yIn, colorIn.intVal, false, false); }
	public ScreenTextObject(String textIn, int xIn, int yIn, int colorIn) { this(textIn, xIn, yIn, colorIn, false, false); }
	public ScreenTextObject(String textIn, int xIn, int yIn, EColors colorIn, boolean centerIn, boolean shadowIn) { this(textIn, xIn, yIn, colorIn.intVal, centerIn, shadowIn); }
	public ScreenTextObject(String textIn, int xIn, int yIn, int colorIn, boolean centerIn, boolean shadowIn) {
		text = textIn;
		xPos = xIn;
		yPos = yIn;
		color = colorIn;
		center = centerIn;
		shadow = shadowIn;
	}
	
	//------------------------
	//ScreenTextObject Getters
	//------------------------
	
	public String getText() { return text; }
	public int getX() { return xPos; }
	public int getY() { return yPos; }
	public int getColor() { return color; }
	public boolean getCenter() { return center; }
	public boolean getShadow() { return shadow; }
	
}
