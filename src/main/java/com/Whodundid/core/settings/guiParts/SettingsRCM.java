package com.Whodundid.core.settings.guiParts;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.gui.SubModErrorType;
import com.Whodundid.core.subMod.util.SubModEnabler;
import com.Whodundid.core.subMod.util.SubModErrorDisplay;
import com.Whodundid.core.util.renderUtil.Resources;

public class SettingsRCM extends EGuiRightClickMenu {

	private SettingsGuiMain window;
	private SubMod mod;
	
	public SettingsRCM(SettingsGuiMain parentIn, int mXIn, int mYIn) { this(parentIn, mXIn, mYIn, null); }
	public SettingsRCM(SettingsGuiMain parentIn, int mXIn, int mYIn, SubMod modIn) {
		super(parentIn, mXIn, mYIn);
		window = parentIn;
		mod = modIn;
		
		addOption("Open", Resources.guiFileUpButton);
		addOption("Open in new Window", Resources.guiFileUpButtonSel);
		
		if (mod.isDisableable()) {
			addOption(mod.isEnabled() ? "Disable" : "Enable", Resources.guiCloseButton);
		}
		
		setRunActionOnPress(true);
		setActionReciever(this);
		
		setUseTitle(true);
		setTitle(mod.getName()).setTitleBackgroundColor(0xff303030).setTitleHeight(14);
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
			IWindowParent g = mod.getMainGui();
			if (g != null) { EnhancedMC.displayEGui(g, getWindowParent()); }
			else { SubModErrorDisplay.displayError(SubModErrorType.NOGUI, mod); }
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void openNewWindow() {
		try {
			IWindowParent g = mod.getMainGui();
			if (g != null) { EnhancedMC.displayEGui(g); }
			else { SubModErrorDisplay.displayError(SubModErrorType.NOGUI, mod); }
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void toggleEnabled() {
		SubModEnabler.toggleEnabled(mod);
		window.updateList();
	}
}
