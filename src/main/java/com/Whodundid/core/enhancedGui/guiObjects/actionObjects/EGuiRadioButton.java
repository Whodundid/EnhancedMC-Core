package com.Whodundid.core.enhancedGui.guiObjects.actionObjects;

import com.Whodundid.core.app.AppConfigSetting;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.enhancedGui.types.EnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.EColors;

//Author: Hunter Bragg

public class EGuiRadioButton extends EnhancedActionObject {

	boolean checked = false;
	int color = EColors.green.intVal;
	
	public EGuiRadioButton(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn) { this(objIn, xIn, yIn, widthIn, heightIn, false); }
	public EGuiRadioButton(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn, AppConfigSetting<Boolean> settingIn) { this(objIn, xIn, yIn, widthIn, heightIn, settingIn.get()); }
	public EGuiRadioButton(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn, boolean checkedIn) {
		init(objIn, xIn, yIn, widthIn, heightIn);
		checked = checkedIn;
	}

	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawFilledEllipse(midX, midY, width / 2, height / 2, 30, EColors.black);
		drawFilledEllipse(midX, midY, width / 2 - 1, height / 2 - 1, 30, EColors.steel);
		
		if (checked) {
			drawFilledEllipse(midX, midY, width / 2 - 3, height / 2 - 3, 30, EColors.green);
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
	
	public EGuiRadioButton updateCheck(AppConfigSetting<Boolean> settingIn, EMCApp m, boolean saveAll) {
		boolean val = settingIn.get();
		if (m != null) {
			if (saveAll) { m.getConfig().saveAllConfigs(); }
			else { m.getConfig().saveMainConfig(); }
		}
		return this;
	}
	
	public boolean isChecked() { return checked; }
	public int getColor() { return color; }
	
	public EGuiRadioButton setChecked(boolean val) { checked = val; return this; }
	public EGuiRadioButton setColor(EColors colorIn) { color = colorIn.c(); return this; }
	public EGuiRadioButton setColor(int colorIn) { color = colorIn; return this; }
}
