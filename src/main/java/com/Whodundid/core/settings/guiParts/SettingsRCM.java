package com.Whodundid.core.settings.guiParts;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.gui.AppErrorType;
import com.Whodundid.core.app.util.AppEnabler;
import com.Whodundid.core.app.util.AppErrorDisplay;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.util.renderUtil.CenterType;

//Author: Hunter Bragg

public class SettingsRCM extends EGuiRightClickMenu {

	private SettingsGuiMain window;
	private EMCApp mod;
	private IWindowParent gui;
	private String title;
	
	public SettingsRCM(SettingsGuiMain parentIn) { this(parentIn, null); }
	public SettingsRCM(SettingsGuiMain parentIn, IWindowParent guiIn, String titleIn) { this (parentIn, null, guiIn, titleIn); }
	public SettingsRCM(SettingsGuiMain parentIn, EMCApp modIn) { this(parentIn, modIn, null, ""); }
	protected SettingsRCM(SettingsGuiMain parentIn, EMCApp modIn, IWindowParent guiIn, String titleIn) {
		window = parentIn;
		mod = modIn;
		gui = guiIn;
		title = titleIn;
	}
	
	@Override
	public void initGui() {
		addOption("Open", EMCResources.guiFileUpButton);
		addOption("Open in new Window", EMCResources.guiFileUpButtonSel);
		
		if (mod != null) {
			if (mod.isDisableable()) { addOption(mod.isEnabled() ? "Disable" : "Enable", EMCResources.guiCloseButton); }
			setTitle(mod.getName()).setTitleBackgroundColor(0xff303030).setTitleHeight(14);
		}
		else {
			setTitle(title);
		}
		
		setRunActionOnPress(true);
		setActionReceiver(this);
		
		setUseTitle(true);
		setBackgroundColor(0xff4b4b4b);
		setSeparatorLineColor(0xff000000);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == this) {
			switch ((String) getSelectedObject()) {
			case "Open": open(); break;
			case "Open in new Window": openNewWindow(); break;
			case "Disable":
			case "Enable": toggleEnabled(); break;
			}
		}
	}
	
	private void open() {
		try {
			IWindowParent g = mod != null ? mod.getMainGui() : gui;
			if (g != null) { EnhancedMC.displayWindow(g, getWindowParent()); }
			else { AppErrorDisplay.displayError(AppErrorType.NOGUI, mod); }
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void openNewWindow() {
		try {
			IWindowParent g = mod != null ? mod.getMainGui() : gui;
			if (g != null) { EnhancedMC.displayWindow(g, window, true, false, false, CenterType.objectIndent); }
			else { AppErrorDisplay.displayError(AppErrorType.NOGUI, mod); }
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void toggleEnabled() {
		AppEnabler.toggleEnabled(mod);
		window.updateList();
	}
}
