package com.Whodundid.core.subMod.util;

import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModSettings;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.subMod.gui.SubModErrorType;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class SubModEnabler {

	public static boolean enableMod(String modNameIn) { return enableMod(RegisteredSubMods.getMod(modNameIn), null); }
	public static boolean enableMod(SubModType typeIn) { return enableMod(RegisteredSubMods.getMod(typeIn), null); }
	public static boolean enableMod(SubMod modIn) { return enableMod(modIn, null); }
	public static boolean enableMod(String modNameIn, ETerminal conIn) { return enableMod(RegisteredSubMods.getMod(modNameIn), conIn); }
	public static boolean enableMod(SubModType typeIn, ETerminal conIn) { return enableMod(RegisteredSubMods.getMod(typeIn), conIn); }
	public static boolean enableMod(SubMod modIn, ETerminal conIn) {
		try {
			if (modIn != null) {
				if (!modIn.isEnabled()) { tryEnable(modIn); }
				
				SubModSettings.updateModState(modIn, true);
				return true;
			}
		}
		catch (SubModToggleException e) {
			if (conIn != null) {
				String message = "Mods: (";
				for (SubMod m : e.getModList()) { message += (m.getName() + ", "); }
				message = message.substring(0, message.length() - 2);
				message += ")";
				message += " are required to enable " + modIn.getName() + ".";
			}
			else { SubModErrorDisplay.displayError(e.getErrorType(), modIn, e.getModList()); }
		}
		catch (Exception e) {
			if (conIn != null) { conIn.badError(e.getMessage()); }
			else { SubModErrorDisplay.displayError(SubModErrorType.ERROR, modIn, e); }
		}
		return false;
	}
	
	public static boolean disableMod(String modNameIn) { return disableMod(RegisteredSubMods.getMod(modNameIn), null); }
	public static boolean disableMod(SubModType typeIn) { return disableMod(RegisteredSubMods.getMod(typeIn), null); }
	public static boolean disableMod(SubMod modIn) { return disableMod(modIn, null); }
	public static boolean disableMod(String modNameIn, ETerminal conIn) { return disableMod(RegisteredSubMods.getMod(modNameIn), conIn); }
	public static boolean disableMod(SubModType typeIn, ETerminal conIn) { return disableMod(RegisteredSubMods.getMod(typeIn), conIn); }
	public static boolean disableMod(SubMod modIn, ETerminal conIn) {
		try {
			if (modIn != null) {
				if (modIn.isEnabled()) {
					if (!modIn.isDisableable()) {
						if (conIn != null) { conIn.error(modIn.getName() + " cannot be disabled!"); }
					}
					else { tryDisable(modIn); }
				}
				
				SubModSettings.updateModState(modIn, false);
				return true;
			}
		}
		catch (SubModToggleException e) {
			if (conIn != null) {
				String message = "Mods: (";
				for (SubMod m : e.getModList()) { message += (m.getName() + ", "); }
				message = message.substring(0, message.length() - 2);
				message += ")";
				message += " require " + modIn.getName() + " to properly function.";
			}
			else { SubModErrorDisplay.displayError(e.getErrorType(), modIn, e.getModList()); }
		}
		catch (Exception e) {
			if (conIn != null) { conIn.badError(e.getMessage()); }
			else { SubModErrorDisplay.displayError(SubModErrorType.ERROR, modIn, e); }
		}
		return false;
	}
	
	public static boolean toggleEnabled(String modNameIn) { return toggleEnabled(RegisteredSubMods.getMod(modNameIn), null); }
	public static boolean toggleEnabled(SubModType typeIn) { return toggleEnabled(RegisteredSubMods.getMod(typeIn), null); }
	public static boolean toggleEnabled(SubMod modIn) { return toggleEnabled(modIn, null); }
	public static boolean toggleEnabled(String modNameIn, ETerminal conIn) { return toggleEnabled(RegisteredSubMods.getMod(modNameIn), conIn); }
	public static boolean toggleEnabled(SubModType typeIn, ETerminal conIn) { return toggleEnabled(RegisteredSubMods.getMod(typeIn), conIn); }
	public static boolean toggleEnabled(SubMod modIn, ETerminal conIn) {
		try {
			if (modIn != null) {
				if (modIn.isEnabled()) {
					if (!modIn.isDisableable()) {
						if (conIn != null) { conIn.error(modIn.getName() + " cannot be disabled!"); }
					}
					else { tryDisable(modIn); }
				}
				else { tryEnable(modIn); }
				
				SubModSettings.updateModState(modIn, !modIn.isEnabled());
				return true;
			}
		} 
		catch (SubModToggleException e) {
			if (conIn != null) {
				String message = "Mods: (";
				for (SubMod m : e.getModList()) { message += (m.getName() + ", "); }
				message = message.substring(0, message.length() - 2);
				message += ")";
				if (modIn.isEnabled()) { message += " require " + modIn.getName() + " to properly function."; }
				else { message += " are required to enable " + modIn.getName() + "."; }
			}
			else { SubModErrorDisplay.displayError(e.getErrorType(), modIn, e.getModList()); }
		}
		catch (Exception e) {
			if (conIn != null) { conIn.badError(e.getMessage()); }
			else { SubModErrorDisplay.displayError(SubModErrorType.ERROR, modIn, e); }
		}
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
