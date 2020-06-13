package com.Whodundid.core.windowLibrary.windowObjects.actionObjects;

import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.config.AppConfigSetting;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.windowLibrary.windowTypes.ActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class WindowCheckBox extends ActionObject {
	
	boolean checked = false;
	boolean drawX = false;
	
	public WindowCheckBox(IWindowObject objIn, int xIn, int yIn, int widthIn, int heightIn) { this(objIn, xIn, yIn, widthIn, heightIn, false); }
	public WindowCheckBox(IWindowObject objIn, int xIn, int yIn, int widthIn, int heightIn, AppConfigSetting<Boolean> settingIn) { this(objIn, xIn, yIn, widthIn, heightIn, settingIn.get()); }
	public WindowCheckBox(IWindowObject objIn, int xIn, int yIn, int widthIn, int heightIn, boolean checkedIn) {
		init(objIn, xIn, yIn, widthIn, heightIn);
		checked = checkedIn;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(startX, startY, endX, endY, EColors.black);
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, EColors.steel);
		
		if (checked) {
			drawTexture(startX - 2, startY - 5, width + 5, height + 6, EMCResources.guiCheck);
		}
		else if (drawX) {
			drawTexture(startX - 2, startY - 2, width + 4, height + 4, EMCResources.guiX);
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
	
	public WindowCheckBox updateCheck(AppConfigSetting<Boolean> settingIn, EMCApp m, boolean saveAll) {
		boolean val = settingIn.get();
		if (m != null) {
			if (saveAll) { m.getConfig().saveAllConfigs(); }
			else { m.getConfig().saveMainConfig(); }
		}
		return this;
	}
	
	public boolean drawsX() { return drawX; }
	public boolean isChecked() { return checked; }
	
	public WindowCheckBox setDrawX(boolean val) { drawX = val; return this; }
	public WindowCheckBox setChecked(boolean val) { checked = val; return this; }
	
}
