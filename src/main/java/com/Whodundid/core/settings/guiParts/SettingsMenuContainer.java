package com.Whodundid.core.settings.guiParts;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.gui.AppErrorType;
import com.Whodundid.core.app.gui.AppInfoWindow;
import com.Whodundid.core.app.util.AppEnabler;
import com.Whodundid.core.app.util.AppErrorDisplay;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedTopParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.storageUtil.EDimension;
import org.lwjgl.input.Keyboard;

//Author: Hunter Bragg

public class SettingsMenuContainer extends EnhancedGuiObject {
	
	EMCApp mod = null;
	SettingsGuiMain window;
	IEnhancedTopParent topParent;
	EGuiButton enable, info, settings;
	int pos = 0;
	
	public SettingsMenuContainer(EGuiScrollList parentIn, EMCApp modIn, int posIn, boolean drawn, SettingsGuiMain windowIn) { this(parentIn, modIn, posIn, 0, drawn, windowIn); }
	public SettingsMenuContainer(EGuiScrollList parentIn, EMCApp modIn, int posIn, int offset, boolean drawn, SettingsGuiMain windowIn) {
		init(parentIn);
		mod = modIn;
		pos = posIn;
		window = windowIn;
		
		topParent = getTopParent();
		
		EDimension d = parentIn.getListDimensions();
		
		int dist = 22;
		
		settings = new EGuiButton(this, d.startX + 6, d.startY + 3 + (pos * dist) + offset, 110, 20, mod != null ? mod.getName() : "ERROR") {
			@Override
			public void mousePressed(int mXIn, int mYIn, int button) {
				super.mousePressed(mXIn, mYIn, button);
				if (button == 1) {
					window.openRCM(mXIn, mYIn, mod);
				}
			}
		};
		
		int enW = drawn ? 50 : 53;
		
		enable = new EGuiButton(this, settings.endX + 11, d.startY + 3 + (pos * dist) + offset, enW, 20, mod != null ? mod.isDisableable() ? (mod.isEnabled() ? "Enabled" : "Disabled") : "Enabled" : "ERROR");
		info = new EGuiButton(this, enable.endX + 4, d.startY + 3 + (pos * dist) + offset, 20, 20);
		
		info.setTextures(EMCResources.guiInfo, EMCResources.guiInfoSel);
		
		settings.setEnabled(mod.isIncompatible() ? false : true);
		enable.setEnabled(mod != null ? (mod.isIncompatible() ? false : mod.isDisableable()) : false);
		
		enable.setDisplayStringColor(mod != null ? (mod.isEnabled() ? 0x55ff55 : 0xff5555) : 0xff5555);
		enable.setDisplayString(mod.isIncompatible() ? "Error" : enable.getDisplayString());
		
		parentIn.addObjectToList(settings, enable, info);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == settings) { openSettings(); }
		if (object == enable) { toggleEnable(); }
		if (object == info) { openInfo(); }
	}
	
	private void openSettings() {
		try {
			EDimension d = topParent.getDimensions();
			IWindowParent gui = mod.getMainGui();
			IWindowParent windowObj = getWindowParent();
			if (gui != null) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) { EnhancedMC.displayWindow(gui); }
				else { EnhancedMC.displayWindow(gui, windowObj, CenterType.object); }
			}
			else { AppErrorDisplay.displayError(AppErrorType.NOGUI, mod); }
		} catch (Exception e) { e.printStackTrace(); System.out.println("Unable to open: " + mod.getName() + "'s main window!"); AppErrorDisplay.displayError(AppErrorType.NOGUI, mod, e); }
	}
	
	private void toggleEnable() {
		if (AppEnabler.toggleEnabled(mod)) {
			enable.setDisplayString(mod.isEnabled() ? "Enabled" : "Disabled");
			enable.setDisplayStringColor(mod.isEnabled() ? 0x55ff55 : 0xff5555);
		}
	}
	
	private void openInfo() {
		EDimension d = topParent.getDimensions();
		AppInfoWindow infoBox = new AppInfoWindow(mod);
		EnhancedMC.displayWindow(infoBox, CenterType.screen);
	}
	
	public int getHeight() { return settings.height; }
}
