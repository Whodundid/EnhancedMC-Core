package com.Whodundid.core.subMod.util;

import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModSettings;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.subMod.gui.SubModErrorType;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class SubModEnabler {

	public static boolean toggleEnabled(String modNameIn) { return toggleEnabled(RegisteredSubMods.getMod(modNameIn)); }
	public static boolean toggleEnabled(SubModType typeIn) { return toggleEnabled(RegisteredSubMods.getMod(typeIn)); }
	public static boolean toggleEnabled(SubMod modIn) {
		try {
			if (modIn != null) {
				if (modIn.isEnabled()) { tryDisable(modIn); }
				else { tryEnable(modIn); }
				
				SubModSettings.updateModState(modIn, !modIn.isEnabled());
				return true;
			}
		} 
		catch (SubModToggleException e) { SubModErrorDisplay.displayError(e.getErrorType(), modIn, e.getModList()); }
		catch (Exception e) { SubModErrorDisplay.displayError(SubModErrorType.ERROR, modIn, e); }
		return false;
	}
	
	private static void tryEnable(SubMod mod) throws SubModToggleException {
		EArrayList<SubMod> incompats = getIncompatibleDependencies(mod);
		
		if (mod.isIncompatible() || incompats.isNotEmpty()) { throw new SubModToggleException(SubModErrorType.INCOMPATIBLE); }
		
		EArrayList<String> allDependencies = RegisteredSubMods.getAllModDependencies(mod);
		EArrayList<SubMod> disabledDependencies = new EArrayList();
		allDependencies.forEach((t) -> { SubMod m = RegisteredSubMods.getMod(t); if (!m.isEnabled()) { disabledDependencies.add(m); } });
		
		if (!disabledDependencies.isEmpty()) {
			throw new SubModToggleException(SubModErrorType.ENABLE, disabledDependencies);
		}
	}
	
	private static void tryDisable(SubMod mod) throws SubModToggleException {
		if (!mod.isDisableable()) { throw new SubModToggleException(SubModErrorType.DISABLE); }
		EArrayList<String> allDependents = RegisteredSubMods.getAllDependantsOfMod(mod);
		EArrayList<SubMod> enabledDependants = new EArrayList();
		allDependents.forEach(t -> { SubMod m = RegisteredSubMods.getMod(t); if (m.isEnabled()) { enabledDependants.add(m); } }); 
		if (!enabledDependants.isEmpty()) {
			throw new SubModToggleException(SubModErrorType.DISABLE, enabledDependants);
		}
	}
	
	private static EArrayList<SubMod> getIncompatibleDependencies(SubMod mod) {
		EArrayList<SubMod> incompatibles = new EArrayList();
		EArrayList<String> allDependencies = RegisteredSubMods.getAllModDependencies(mod);
		for (String s : allDependencies) {
			SubMod m = RegisteredSubMods.getMod(s);
			if (m != null && m.isIncompatible()) { incompatibles.add(m); }
		}
		return incompatibles;
	}
}
