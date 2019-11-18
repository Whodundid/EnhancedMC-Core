package com.Whodundid.core.settings.guiParts;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiScrollList;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedTopParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.gui.SubModErrorType;
import com.Whodundid.core.subMod.gui.SubModInfoDialogueBox;
import com.Whodundid.core.subMod.util.SubModEnabler;
import com.Whodundid.core.subMod.util.SubModErrorDisplay;
import com.Whodundid.core.util.miscUtil.CenterType;
import com.Whodundid.core.util.renderUtil.Resources;
import com.Whodundid.core.util.storageUtil.EDimension;
import org.lwjgl.input.Keyboard;

public class SettingsMenuContainer extends EnhancedGuiObject {
	
	SubMod mod = null;
	SettingsGuiMain window;
	IEnhancedTopParent topParent;
	EGuiButton enable, info, settings;
	int pos = 0;
	
	public SettingsMenuContainer(EGuiScrollList parentIn, SubMod modIn, int posIn, SettingsGuiMain windowIn) { this(parentIn, modIn, posIn, 0, windowIn); }
	public SettingsMenuContainer(EGuiScrollList parentIn, SubMod modIn, int posIn, int offset, SettingsGuiMain windowIn) {
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
		
		enable = new EGuiButton(this, settings.endX + 11, d.startY + 3 + (pos * dist) + offset, 50, 20, mod != null ? mod.isDisableable() ? (mod.isEnabled() ? "Enabled" : "Disabled") : "Enabled" : "ERROR");
		info = new EGuiButton(this, enable.endX + 4, d.startY + 3 + (pos * dist) + offset, 20, 20);
		
		info.setTextures(Resources.guiInfo, Resources.guiInfoSel);
		
		settings.setEnabled(mod.isIncompatible() ? false : true);
		enable.setEnabled(mod != null ? (mod.isIncompatible() ? false : mod.isDisableable()) : false);
		
		enable.setDisplayStringColor(mod != null ? (mod.isEnabled() ? 0x55ff55 : 0xff5555) : 0xff5555);
		enable.setDisplayString(mod.isIncompatible() ? "Error" : enable.getDisplayString());
		
		parentIn.addObjectToList(settings, enable, info);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
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
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) { EnhancedMC.displayEGui(gui); }
				else { EnhancedMC.displayEGui(gui, windowObj, CenterType.object); }
			}
			else { SubModErrorDisplay.displayError(SubModErrorType.NOGUI, mod); }
		} catch (Exception e) { e.printStackTrace(); System.out.println("Unable to open: " + mod.getName() + "'s main gui!"); SubModErrorDisplay.displayError(SubModErrorType.NOGUI, mod, e); }
	}
	
	private void toggleEnable() {
		if (SubModEnabler.toggleEnabled(mod)) {
			enable.setDisplayString(mod.isEnabled() ? "Enabled" : "Disabled");
			enable.setDisplayStringColor(mod.isEnabled() ? 0x55ff55 : 0xff5555);
		}
	}
	
	private void openInfo() {
		EDimension d = topParent.getDimensions();
		SubModInfoDialogueBox infoBox = new SubModInfoDialogueBox(topParent, mod);
		topParent.addObject(infoBox);
	}
	
	public int getHeight() { return settings.height; }
}
