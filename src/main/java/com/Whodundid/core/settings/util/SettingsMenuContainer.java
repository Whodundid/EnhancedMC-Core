package com.Whodundid.core.settings.util;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.util.AppEnabler;
import com.Whodundid.core.app.util.AppErrorDisplay;
import com.Whodundid.core.app.window.AppInfoWindow;
import com.Whodundid.core.app.window.windowUtil.AppErrorType;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.settings.SettingsWindowMain;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.scrollList.WindowScrollList;
import com.Whodundid.core.windowLibrary.windowTypes.WindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.ITopParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowParent;
import org.lwjgl.input.Keyboard;

//Author: Hunter Bragg

public class SettingsMenuContainer extends WindowObject {
	
	EMCApp mod = null;
	SettingsWindowMain window;
	ITopParent topParent;
	WindowButton enable, info, settings;
	int pos = 0;
	
	public SettingsMenuContainer(WindowScrollList parentIn, EMCApp modIn, int posIn, boolean drawn, SettingsWindowMain windowIn) { this(parentIn, modIn, posIn, 0, drawn, windowIn); }
	public SettingsMenuContainer(WindowScrollList parentIn, EMCApp modIn, int posIn, int offset, boolean drawn, SettingsWindowMain windowIn) {
		init(parentIn);
		mod = modIn;
		pos = posIn;
		window = windowIn;
		
		topParent = getTopParent();
		
		EDimension d = parentIn.getListDimensions();
		
		int dist = 22;
		
		settings = new WindowButton(this, d.startX + 6, d.startY + 3 + (pos * dist) + offset, 110, 20, mod != null ? mod.getName() : "ERROR") {
			@Override
			public void mousePressed(int mXIn, int mYIn, int button) {
				super.mousePressed(mXIn, mYIn, button);
				if (button == 1) {
					window.openRCM(mXIn, mYIn, mod);
				}
			}
		};
		
		int enW = drawn ? 50 : 53;
		
		enable = new WindowButton(this, settings.endX + 11, d.startY + 3 + (pos * dist) + offset, enW, 20, mod != null ? mod.isDisableable() ? (mod.isEnabled() ? "Enabled" : "Disabled") : "Enabled" : "ERROR");
		info = new WindowButton(this, enable.endX + 4, d.startY + 3 + (pos * dist) + offset, 20, 20);
		
		info.setTextures(EMCResources.guiInfo, EMCResources.guiInfoSel);
		
		settings.setEnabled(mod.isIncompatible() ? false : true);
		enable.setEnabled(mod != null ? (mod.isIncompatible() || !mod.canBeEnabled() ? false : mod.isDisableable()) : false);
		
		enable.setStringColor(mod != null ? (mod.isEnabled() ? 0x55ff55 : 0xff5555) : 0xff5555);
		enable.setString(mod.isIncompatible() ? "Error" : enable.getString());
		
		parentIn.addObjectToList(settings, enable, info);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == settings) { openSettings(); }
		if (object == enable) { toggleEnable(); }
		if (object == info) { openInfo(); }
	}
	
	private void openSettings() {
		try {
			EDimension d = topParent.getDimensions();
			IWindowParent gui = mod.getMainWindow();
			IWindowParent windowObj = getWindowParent();
			if (gui != null) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) { EnhancedMC.displayWindow(gui); }
				else { EnhancedMC.displayWindow(gui, windowObj, CenterType.object); }
			}
			else { AppErrorDisplay.displayError(AppErrorType.NOGUI, mod); }
		}
		catch (Exception e) { e.printStackTrace(); System.out.println("Unable to open: " + mod.getName() + "'s main window!"); AppErrorDisplay.displayError(AppErrorType.NOGUI, mod, e); }
	}
	
	private void toggleEnable() {
		if (AppEnabler.toggleEnabled(mod)) {
			enable.setString(mod.isEnabled() ? "Enabled" : "Disabled");
			enable.setStringColor(mod.isEnabled() ? 0x55ff55 : 0xff5555);
		}
	}
	
	private void openInfo() {
		EDimension d = topParent.getDimensions();
		AppInfoWindow infoBox = new AppInfoWindow(mod);
		EnhancedMC.displayWindow(infoBox, CenterType.screen);
	}
	
	public int getHeight() { return settings.height; }
	
}
