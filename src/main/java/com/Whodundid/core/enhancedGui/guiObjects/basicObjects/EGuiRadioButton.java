package com.Whodundid.core.enhancedGui.guiObjects.basicObjects;

import com.Whodundid.core.enhancedGui.types.EnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.EColors;

public class EGuiRadioButton extends EnhancedActionObject {

	boolean checked = false;
	
	public EGuiRadioButton(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn) { this(objIn, xIn, yIn, widthIn, heightIn, false); }
	public EGuiRadioButton(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn, boolean checkedIn) {
		init(objIn, xIn, yIn, widthIn, heightIn);
		checked = checkedIn;
	}

	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawFilledCircle(midX, midY, width / 2, 30, EColors.black);
		drawFilledCircle(midX, midY, width / 2 - 1, 30, EColors.steel);
		
		if (checked) {
			drawFilledCircle(midX, midY, width / 2 - 4, 30, EColors.green);
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 0) {
			EGuiButton.playPressSound();
			checked = !checked;
			performAction();
		}
	}
	
	public boolean isChecked() { return checked; }
	
	public EGuiRadioButton setChecked(boolean val) { checked = val; return this; }
}
