package com.Whodundid.core.settings.guiParts;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreSubMod.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.gui.SubModErrorType;
import com.Whodundid.core.subMod.util.SubModEnabler;
import com.Whodundid.core.subMod.util.SubModErrorDisplay;
import com.Whodundid.core.util.renderUtil.CenterType;

//Author: Hunter Bragg

public class SettingsRCM extends EGuiRightClickMenu {

	private SettingsGuiMain window;
	private SubMod mod;
	private IWindowParent gui;
	private String title;
	
	public SettingsRCM(SettingsGuiMain parentIn) { this(parentIn, null); }
	public SettingsRCM(SettingsGuiMain parentIn, IWindowParent guiIn, String titleIn) { this (parentIn, null, guiIn, titleIn); }
	public SettingsRCM(SettingsGuiMain parentIn, SubMod modIn) { this(parentIn, modIn, null, ""); }
	protected SettingsRCM(SettingsGuiMain parentIn, SubMod modIn, IWindowParent guiIn, String titleIn) {
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
		setActionReciever(this);
		
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
			if (g != null) { EnhancedMC.displayEGui(g, getWindowParent()); }
			else { SubModErrorDisplay.displayError(SubModErrorType.NOGUI, mod); }
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void openNewWindow() {
		try {
			IWindowParent g = mod != null ? mod.getMainGui() : gui;
			if (g != null) { EnhancedMC.displayEGui(g, window, true, false, false, CenterType.objectIndent); }
			else { SubModErrorDisplay.displayError(SubModErrorType.NOGUI, mod); }
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void toggleEnabled() {
		SubModEnabler.toggleEnabled(mod);
		window.updateList();
	}
}
