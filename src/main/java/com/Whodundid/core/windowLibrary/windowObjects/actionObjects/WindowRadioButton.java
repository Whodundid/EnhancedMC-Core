package com.Whodundid.core.windowLibrary.windowObjects.actionObjects;

import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.config.AppConfigSetting;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.windowLibrary.windowTypes.ActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class WindowRadioButton extends ActionObject {

	boolean checked = false;
	int color = EColors.green.intVal;
	
	public WindowRadioButton(IWindowObject objIn, int xIn, int yIn, int widthIn, int heightIn) { this(objIn, xIn, yIn, widthIn, heightIn, false); }
	public WindowRadioButton(IWindowObject objIn, int xIn, int yIn, int widthIn, int heightIn, AppConfigSetting<Boolean> settingIn) { this(objIn, xIn, yIn, widthIn, heightIn, settingIn.get()); }
	public WindowRadioButton(IWindowObject objIn, int xIn, int yIn, int widthIn, int heightIn, boolean checkedIn) {
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
			WindowButton.playPressSound();
			checked = !checked;
			performAction();
		}
	}
	
	public WindowRadioButton updateCheck(AppConfigSetting<Boolean> settingIn, EMCApp m, boolean saveAll) {
		boolean val = settingIn.get();
		if (m != null) {
			if (saveAll) { m.getConfig().saveAllConfigs(); }
			else { m.getConfig().saveMainConfig(); }
		}
		return this;
	}
	
	public boolean isChecked() { return checked; }
	public int getColor() { return color; }
	
	public WindowRadioButton setChecked(boolean val) { checked = val; return this; }
	public WindowRadioButton setColor(EColors colorIn) { color = colorIn.c(); return this; }
	public WindowRadioButton setColor(int colorIn) { color = colorIn; return this; }
	
}
