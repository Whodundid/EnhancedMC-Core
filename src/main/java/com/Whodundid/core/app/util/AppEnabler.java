package com.Whodundid.core.app.util;

import com.Whodundid.core.app.AppSettings;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.app.gui.AppErrorType;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class AppEnabler {

	public static boolean enableMod(String modNameIn) { return enableMod(RegisteredApps.getApp(modNameIn), null); }
	public static boolean enableMod(AppType typeIn) { return enableMod(RegisteredApps.getApp(typeIn), null); }
	public static boolean enableMod(EMCApp modIn) { return enableMod(modIn, null); }
	public static boolean enableMod(String modNameIn, ETerminal conIn) { return enableMod(RegisteredApps.getApp(modNameIn), conIn); }
	public static boolean enableMod(AppType typeIn, ETerminal conIn) { return enableMod(RegisteredApps.getApp(typeIn), conIn); }
	public static boolean enableMod(EMCApp modIn, ETerminal conIn) {
		try {
			if (modIn != null) {
				if (!modIn.isEnabled()) { tryEnable(modIn); }
				
				AppSettings.updateAppState(modIn, true);
				return true;
			}
		}
		catch (AppToggleException e) {
			if (conIn != null) {
				String message = "Apps: (";
				for (EMCApp m : e.getModList()) { message += (m.getName() + ", "); }
				message = message.substring(0, message.length() - 2);
				message += ")";
				message += " are required to enable " + modIn.getName() + ".";
				conIn.error(message);
			}
			else { AppErrorDisplay.displayError(e.getErrorType(), modIn, e.getModList()); }
		}
		catch (Exception e) {
			if (conIn != null) { conIn.badError(e.getMessage()); }
			else { AppErrorDisplay.displayError(AppErrorType.ERROR, modIn, e); }
		}
		return false;
	}
	
	public static boolean disableMod(String modNameIn) { return disableMod(RegisteredApps.getApp(modNameIn), null); }
	public static boolean disableMod(AppType typeIn) { return disableMod(RegisteredApps.getApp(typeIn), null); }
	public static boolean disableMod(EMCApp modIn) { return disableMod(modIn, null); }
	public static boolean disableMod(String modNameIn, ETerminal conIn) { return disableMod(RegisteredApps.getApp(modNameIn), conIn); }
	public static boolean disableMod(AppType typeIn, ETerminal conIn) { return disableMod(RegisteredApps.getApp(typeIn), conIn); }
	public static boolean disableMod(EMCApp modIn, ETerminal conIn) {
		try {
			if (modIn != null) {
				if (modIn.isEnabled()) {
					if (!modIn.isDisableable()) {
						if (conIn != null) { conIn.error(modIn.getName() + " cannot be disabled!"); }
						return false;
					}
					tryDisable(modIn);
				}
				
				AppSettings.updateAppState(modIn, false);
				return true;
			}
		}
		catch (AppToggleException e) {
			if (conIn != null) {
				String message = "Apps: (";
				for (EMCApp m : e.getModList()) { message += (m.getName() + ", "); }
				message = message.substring(0, message.length() - 2);
				message += ")";
				message += " require " + modIn.getName() + " to properly function.";
				conIn.error(message);
			}
			else { AppErrorDisplay.displayError(e.getErrorType(), modIn, e.getModList()); }
		}
		catch (Exception e) {
			if (conIn != null) { conIn.badError(e.getMessage()); }
			else { AppErrorDisplay.displayError(AppErrorType.ERROR, modIn, e); }
		}
		return false;
	}
	
	public static boolean toggleEnabled(String modNameIn) { return toggleEnabled(RegisteredApps.getApp(modNameIn), null); }
	public static boolean toggleEnabled(AppType typeIn) { return toggleEnabled(RegisteredApps.getApp(typeIn), null); }
	public static boolean toggleEnabled(EMCApp modIn) { return toggleEnabled(modIn, null); }
	public static boolean toggleEnabled(String modNameIn, ETerminal conIn) { return toggleEnabled(RegisteredApps.getApp(modNameIn), conIn); }
	public static boolean toggleEnabled(AppType typeIn, ETerminal conIn) { return toggleEnabled(RegisteredApps.getApp(typeIn), conIn); }
	public static boolean toggleEnabled(EMCApp modIn, ETerminal conIn) {
		try {
			if (modIn != null) {
				if (modIn.isEnabled()) {
					if (!modIn.isDisableable()) {
						if (conIn != null) { conIn.error(modIn.getName() + " cannot be disabled!"); }
					}
					else { tryDisable(modIn); }
				}
				else { tryEnable(modIn); }
				
				AppSettings.updateAppState(modIn, !modIn.isEnabled());
				return true;
			}
		} 
		catch (AppToggleException e) {
			if (conIn != null) {
				String message = "Apps: (";
				for (EMCApp m : e.getModList()) { message += (m.getName() + ", "); }
				message = message.substring(0, message.length() - 2);
				message += ")";
				if (modIn.isEnabled()) { message += " require " + modIn.getName() + " to properly function."; }
				else { message += " are required to enable " + modIn.getName() + "."; }
			}
			else { AppErrorDisplay.displayError(e.getErrorType(), modIn, e.getModList()); }
		}
		catch (Exception e) {
			if (conIn != null) { conIn.badError(e.getMessage()); }
			else { AppErrorDisplay.displayError(AppErrorType.ERROR, modIn, e); }
		}
		return false;
	}
	
	private static void tryEnable(EMCApp mod) throws AppToggleException {
		EArrayList<EMCApp> incompats = getIncompatibleDependencies(mod);
		
		if (mod.isIncompatible() || incompats.isNotEmpty()) { throw new AppToggleException(AppErrorType.INCOMPATIBLE); }
		
		EArrayList<String> allDependencies = RegisteredApps.getAllAppDependencies(mod);
		EArrayList<EMCApp> disabledDependencies = new EArrayList();
		allDependencies.forEach((t) -> { EMCApp m = RegisteredApps.getApp(t); if (!m.isEnabled()) { disabledDependencies.add(m); } });
		
		if (!disabledDependencies.isEmpty()) {
			throw new AppToggleException(AppErrorType.ENABLE, disabledDependencies);
		}
	}
	
	private static void tryDisable(EMCApp mod) throws AppToggleException {
		if (!mod.isDisableable()) { throw new AppToggleException(AppErrorType.DISABLE); }
		EArrayList<String> allDependents = RegisteredApps.getAllDependantsOfApp(mod);
		EArrayList<EMCApp> enabledDependants = new EArrayList();
		allDependents.forEach(t -> { EMCApp m = RegisteredApps.getApp(t); if (m.isEnabled()) { enabledDependants.add(m); } }); 
		if (!enabledDependants.isEmpty()) {
			throw new AppToggleException(AppErrorType.DISABLE, enabledDependants);
		}
	}
	
	private static EArrayList<EMCApp> getIncompatibleDependencies(EMCApp mod) {
		EArrayList<EMCApp> incompatibles = new EArrayList();
		EArrayList<String> allDependencies = RegisteredApps.getAllAppDependencies(mod);
		for (String s : allDependencies) {
			EMCApp m = RegisteredApps.getApp(s);
			if (m != null && m.isIncompatible()) { incompatibles.add(m); }
		}
		return incompatibles;
	}
}
