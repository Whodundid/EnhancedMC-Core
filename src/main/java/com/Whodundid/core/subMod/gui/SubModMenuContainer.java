package com.Whodundid.core.subMod.gui;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.InnerEnhancedGui;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiScrollList;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedTopParent;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModSettings;
import com.Whodundid.core.util.renderUtil.Resources;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;

public class SubModMenuContainer extends EnhancedGuiObject {
	
	SubMod mod = null;
	IEnhancedTopParent topParent;
	EGuiButton settings, enable, info;
	int pos = 0;
	
	public SubModMenuContainer(EGuiScrollList parentIn, SubMod modIn, int posIn) { this(parentIn, modIn, posIn, 0); }
	public SubModMenuContainer(EGuiScrollList parentIn, SubMod modIn, int posIn, int offset) {
		init(parentIn);
		mod = modIn;
		pos = posIn;
		
		topParent = getTopParent();
		
		EDimension d = parentIn.getListDimensions();
		
		int dist = 22;
		
		settings = new EGuiButton(this, d.startX + 6, d.startY + 3 + (pos * dist) + offset, 110, 20, mod != null ? mod.getName() : "ERROR");
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
	
	public int getHeight() { return settings.height; }
	
	private void openSettings() {
		try {
			EDimension d = topParent.getDimensions();
			InnerEnhancedGui gui = mod.getMainGui();
			IEnhancedGuiObject windowObj = getWindowParent();
			if (gui != null && windowObj instanceof InnerEnhancedGui) {
				InnerEnhancedGui window = (InnerEnhancedGui) windowObj;
				window.close();
				window.getGuiHistory().add(window);
				gui.setGuiHistory(window.getGuiHistory());
				EnhancedMC.displayEGui(gui);
				gui.setPosition(window.startX, window.startY);
			}
			else { displayError(SubModErrorType.NOGUI); }
		} catch (Exception e) { System.out.println("Unable to open: " + mod.getName() + "'s main gui!"); displayError(SubModErrorType.NOGUI, e); }
	}
	
	private void toggleEnable() {
		try {
			if (mod != null) {
				if (mod.isEnabled()) { tryDisable(); }
				else { tryEnable(); }
				
				SubModSettings.updateModState(mod, !mod.isEnabled());
				enable.setDisplayString(mod.isEnabled() ? "Enabled" : "Disabled");
				enable.setDisplayStringColor(mod.isEnabled() ? 0x55ff55 : 0xff5555);
			}
		} 
		catch (SubModToggleException e) {}
		catch (Exception e) { displayError(SubModErrorType.ERROR, e); }
	}
	
	private void tryEnable() throws SubModToggleException {
		EArrayList<SubMod> incompats = getIncompatibleDependencies();
		
		if (mod.isIncompatible() || incompats.isNotEmpty()) { displayError(SubModErrorType.INCOMPATIBLE); throw new SubModToggleException(); }
		
		EArrayList<String> allDependencies = RegisteredSubMods.getAllModDependencies(mod);
		EArrayList<SubMod> disabledDependancies = new EArrayList();
		allDependencies.forEach((t) -> { SubMod m = RegisteredSubMods.getMod(t); if (!m.isEnabled()) { disabledDependancies.add(m); } });
		
		if (!disabledDependancies.isEmpty()) {
			displayError(SubModErrorType.ENABLE, disabledDependancies);
			throw new SubModToggleException();
		}
	}
	
	private void tryDisable() throws SubModToggleException {
		EArrayList<String> allDependents = RegisteredSubMods.getAllDependantsOfMod(mod);
		EArrayList<SubMod> enabledDependants = new EArrayList();
		allDependents.forEach(t -> { SubMod m = RegisteredSubMods.getMod(t); if (m.isEnabled()) { enabledDependants.add(m); } }); 
		if (!enabledDependants.isEmpty()) {
			displayError(SubModErrorType.DISABLE, enabledDependants);
			throw new SubModToggleException();
		}
	}
	
	private EArrayList<SubMod> getIncompatibleDependencies() {
		EArrayList<SubMod> incompatibles = new EArrayList();
		EArrayList<String> allDependencies = RegisteredSubMods.getAllModDependencies(mod);
		for (String s : allDependencies) {
			SubMod m = RegisteredSubMods.getMod(s);
			if (m != null && m.isIncompatible()) { incompatibles.add(m); }
		}
		return incompatibles;
	}
	
	private void openInfo() {
		EDimension d = topParent.getDimensions();
		SubModInfoDialogueBox infoBox = new SubModInfoDialogueBox(topParent, mod);
		topParent.addObject(infoBox);
	}
	
	private void displayError(SubModErrorType type) { displayError(type, null, null); }
	private void displayError(SubModErrorType type, Exception e) { displayError(type, e, null); }
	private void displayError(SubModErrorType type, EArrayList<SubMod> mods) { displayError(type, null, mods); }
	private void displayError(SubModErrorType type, Exception e, EArrayList<SubMod> mods) {
		EDimension d = topParent.getDimensions();
		SubModErrorDialogueBox errorBox = new SubModErrorDialogueBox(topParent, type, mod);
		topParent.addObject(errorBox);
		errorBox.createErrorMessage(e, mods);
	}
	
	private class SubModToggleException extends Exception {}
}
