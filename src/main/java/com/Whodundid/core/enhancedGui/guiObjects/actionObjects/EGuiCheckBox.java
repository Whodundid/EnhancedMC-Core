package com.Whodundid.core.enhancedGui.guiObjects.actionObjects;

import com.Whodundid.core.app.AppConfigSetting;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.types.EnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.EColors;

//Author: Hunter Bragg

public class EGuiCheckBox extends EnhancedActionObject {
	
	boolean checked = false;
	
	public EGuiCheckBox(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn) { this(objIn, xIn, yIn, widthIn, heightIn, false); }
	public EGuiCheckBox(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn, AppConfigSetting<Boolean> settingIn) { this(objIn, xIn, yIn, widthIn, heightIn, settingIn.get()); }
	public EGuiCheckBox(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn, boolean checkedIn) {
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
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 0) {
			EGuiButton.playPressSound();
			checked = !checked;
			performAction();
		}
	}
	
	public EGuiCheckBox updateCheck(AppConfigSetting<Boolean> settingIn, EMCApp m, boolean saveAll) {
		boolean val = settingIn.get();
		if (m != null) {
			if (saveAll) { m.getConfig().saveAllConfigs(); }
			else { m.getConfig().saveMainConfig(); }
		}
		return this;
	}
	
	public boolean isChecked() { return checked; }
	
	public EGuiCheckBox setChecked(boolean val) { checked = val; return this; }
}
