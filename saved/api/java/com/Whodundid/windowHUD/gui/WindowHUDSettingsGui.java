package com.Whodundid.windowHUD.gui;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.windowHUD.WindowHUDApp;

public class WindowHUDSettingsGui extends WindowParent {
	
	WindowHUDApp mod = (WindowHUDApp) RegisteredApps.getApp(AppType.WINDOWHUD);
	
	EGuiButton wHotbar, wScoreboard, wTabList;
	EGuiLabel hotBarLabel, scoreboardLabel, tabListLabel;
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
	}

	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		
	}
}
