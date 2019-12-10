package com.Whodundid.core.settings.guiParts;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.gui.SubModErrorType;
import com.Whodundid.core.subMod.util.SubModEnabler;
import com.Whodundid.core.subMod.util.SubModErrorDisplay;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.Resources;

public class SettingsRCM extends EGuiRightClickMenu {

	private SettingsGuiMain window;
	private SubMod mod;
	private IWindowParent gui;
	
	public SettingsRCM(SettingsGuiMain parentIn, int mXIn, int mYIn) { this(parentIn, mXIn, mYIn, null); }
	public SettingsRCM(SettingsGuiMain parentIn, int mXIn, int mYIn, IWindowParent guiIn, String titleIn) { this (parentIn, mXIn, mYIn, null, guiIn, titleIn); }
	public SettingsRCM(SettingsGuiMain parentIn, int mXIn, int mYIn, SubMod modIn) { this(parentIn, mXIn, mYIn, modIn, null, ""); }
	protected SettingsRCM(SettingsGuiMain parentIn, int mXIn, int mYIn, SubMod modIn, IWindowParent guiIn, String titleIn) {
		super(parentIn, mXIn, mYIn);
		window = parentIn;
		mod = modIn;
		gui = guiIn;
		
		addOption("Open", Resources.guiFileUpButton);
		addOption("Open in new Window", Resources.guiFileUpButtonSel);
		
		if (mod != null) {
			if (mod.isDisableable()) { addOption(mod.isEnabled() ? "Disable" : "Enable", Resources.guiCloseButton); }
			setTitle(mod.getName()).setTitleBackgroundColor(0xff303030).setTitleHeight(14);
		}
		else {
			setTitle(titleIn);
		}
		
		setRunActionOnPress(true);
		setActionReciever(this);
		
		setUseTitle(true);
		setBackgroundColor(0xff4b4b4b);
		setSeparatorLineColor(0xff000000);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
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
			if (g != null) { EnhancedMC.displayEGui(g, window, false, false, CenterType.objectIndent); }
			else { SubModErrorDisplay.displayError(SubModErrorType.NOGUI, mod); }
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void toggleEnabled() {
		SubModEnabler.toggleEnabled(mod);
		window.updateList();
	}
}
