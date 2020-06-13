package com.Whodundid.core.app.util;

import com.Whodundid.core.app.AppSettings;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.app.window.windowUtil.AppErrorType;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class AppEnabler {

	public static boolean enableApp(String appNameIn) { return enableApp(RegisteredApps.getApp(appNameIn), null); }
	public static boolean enableApp(AppType typeIn) { return enableApp(RegisteredApps.getApp(typeIn), null); }
	public static boolean enableApp(EMCApp appIn) { return enableApp(appIn, null); }
	public static boolean enableApp(String appNameIn, ETerminal termIn) { return enableApp(RegisteredApps.getApp(appNameIn), termIn); }
	public static boolean enableApp(AppType typeIn, ETerminal termIn) { return enableApp(RegisteredApps.getApp(typeIn), termIn); }
	public static boolean enableApp(EMCApp appIn, ETerminal termIn) {
		try {
			if (appIn != null) {
				if (!appIn.canBeEnabled()) {
					if (termIn != null) { termIn.error(appIn.getName() + " cannot be enabled!"); }
					return false;
				}
				if (!appIn.isEnabled()) { tryEnable(appIn); }
				
				AppSettings.updateAppState(appIn, true);
				return true;
			}
		}
		catch (AppToggleException e) {
			if (termIn != null) {
				String message = "Apps: (";
				for (EMCApp m : e.getModList()) { message += (m.getName() + ", "); }
				message = message.substring(0, message.length() - 2);
				message += ")";
				message += " are required to enable " + appIn.getName() + ".";
				termIn.error(message);
			}
			else { AppErrorDisplay.displayError(e.getErrorType(), appIn, e.getModList()); }
		}
		catch (Exception e) {
			if (termIn != null) {
				StackTraceElement[] trace = e.getStackTrace();
				String errLoc = (trace != null && trace[0] != null) ? "\n" + trace[0].toString() : null;
				termIn.javaError(e.toString() + errLoc);
			}
			else { AppErrorDisplay.displayError(AppErrorType.ERROR, appIn, e); }
		}
		return false;
	}
	
	public static boolean disableApp(String appNameIn) { return disableApp(RegisteredApps.getApp(appNameIn), null); }
	public static boolean disableApp(AppType typeIn) { return disableApp(RegisteredApps.getApp(typeIn), null); }
	public static boolean disableApp(EMCApp appIn) { return disableApp(appIn, null); }
	public static boolean disableApp(String appNameIn, ETerminal termIn) { return disableApp(RegisteredApps.getApp(appNameIn), termIn); }
	public static boolean disableApp(AppType typeIn, ETerminal termIn) { return disableApp(RegisteredApps.getApp(typeIn), termIn); }
	public static boolean disableApp(EMCApp appIn, ETerminal termIn) {
		try {
			if (appIn != null) {
				if (appIn.isEnabled()) {
					if (!appIn.isDisableable()) {
						if (termIn != null) { termIn.error(appIn.getName() + " cannot be disabled!"); }
						return false;
					}
					tryDisable(appIn);
				}
				
				AppSettings.updateAppState(appIn, false);
				return true;
			}
		}
		catch (AppToggleException e) {
			if (termIn != null) {
				String message = "Apps: (";
				for (EMCApp m : e.getModList()) { message += (m.getName() + ", "); }
				message = message.substring(0, message.length() - 2);
				message += ")";
				message += " require " + appIn.getName() + " to properly function.";
				termIn.error(message);
			}
			else { AppErrorDisplay.displayError(e.getErrorType(), appIn, e.getModList()); }
		}
		catch (Exception e) {
			if (termIn != null) {
				StackTraceElement[] trace = e.getStackTrace();
				String errLoc = (trace != null && trace[0] != null) ? "\n" + trace[0].toString() : null;
				termIn.javaError(e.toString() + errLoc);
			}
			else { AppErrorDisplay.displayError(AppErrorType.ERROR, appIn, e); }
		}
		return false;
	}
	
	public static boolean toggleEnabled(String appNameIn) { return toggleEnabled(RegisteredApps.getApp(appNameIn), null); }
	public static boolean toggleEnabled(AppType typeIn) { return toggleEnabled(RegisteredApps.getApp(typeIn), null); }
	public static boolean toggleEnabled(EMCApp appIn) { return toggleEnabled(appIn, null); }
	public static boolean toggleEnabled(String appNameIn, ETerminal termIn) { return toggleEnabled(RegisteredApps.getApp(appNameIn), termIn); }
	public static boolean toggleEnabled(AppType typeIn, ETerminal termIn) { return toggleEnabled(RegisteredApps.getApp(typeIn), termIn); }
	public static boolean toggleEnabled(EMCApp appIn, ETerminal termIn) {
		try {
			if (appIn != null) {
				if (appIn.isEnabled()) {
					if (!appIn.isDisableable()) {
						if (termIn != null) { termIn.error(appIn.getName() + " cannot be disabled!"); }
					}
					else { tryDisable(appIn); }
				}
				else { tryEnable(appIn); }
				
				AppSettings.updateAppState(appIn, !appIn.isEnabled());
				return true;
			}
		} 
		catch (AppToggleException e) {
			if (termIn != null) {
				String message = "Apps: (";
				for (EMCApp m : e.getModList()) { message += (m.getName() + ", "); }
				message = message.substring(0, message.length() - 2);
				message += ")";
				if (appIn.isEnabled()) { message += " require " + appIn.getName() + " to properly function."; }
				else { message += " are required to enable " + appIn.getName() + "."; }
			}
			else { AppErrorDisplay.displayError(e.getErrorType(), appIn, e.getModList()); }
		}
		catch (Exception e) {
			if (termIn != null) {
				StackTraceElement[] trace = e.getStackTrace();
				String errLoc = (trace != null && trace[0] != null) ? "\n" + trace[0].toString() : null;
				termIn.javaError(e.toString() + errLoc);
			}
			else { AppErrorDisplay.displayError(AppErrorType.ERROR, appIn, e); }
		}
		return false;
	}
	
	private static void tryEnable(EMCApp app) throws AppToggleException {
		EArrayList<EMCApp> incompats = getIncompatibleDependencies(app);
		
		if (app.isIncompatible() || incompats.isNotEmpty()) { throw new AppToggleException(AppErrorType.INCOMPATIBLE); }
		
		EArrayList<String> allDependencies = RegisteredApps.getAllAppDependencies(app);
		EArrayList<EMCApp> disabledDependencies = new EArrayList();
		allDependencies.forEach((t) -> { EMCApp m = RegisteredApps.getApp(t); if (!m.isEnabled()) { disabledDependencies.add(m); } });
		
		if (!disabledDependencies.isEmpty()) {
			throw new AppToggleException(AppErrorType.ENABLE, disabledDependencies);
		}
	}
	
	private static void tryDisable(EMCApp app) throws AppToggleException {
		if (!app.isDisableable()) { throw new AppToggleException(AppErrorType.DISABLE); }
		EArrayList<String> allDependents = RegisteredApps.getAllDependantsOfApp(app);
		EArrayList<EMCApp> enabledDependants = new EArrayList();
		allDependents.forEach(t -> { EMCApp m = RegisteredApps.getApp(t); if (m.isEnabled()) { enabledDependants.add(m); } }); 
		if (!enabledDependants.isEmpty()) {
			throw new AppToggleException(AppErrorType.DISABLE, enabledDependants);
		}
	}
	
	private static EArrayList<EMCApp> getIncompatibleDependencies(EMCApp app) {
		EArrayList<EMCApp> incompatibles = new EArrayList();
		EArrayList<String> allDependencies = RegisteredApps.getAllAppDependencies(app);
		for (String s : allDependencies) {
			EMCApp m = RegisteredApps.getApp(s);
			if (m != null && m.isIncompatible()) { incompatibles.add(m); }
		}
		return incompatibles;
	}
	
}
