package com.Whodundid.core.app;

import org.apache.logging.log4j.Level;
import java.io.File;
import java.util.Collections;
import java.util.List;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;

//Author: Hunter Bragg

public final class RegisteredApps {
	
	private static EArrayList<EMCApp> allApps = new EArrayList();
	private static EArrayList<EMCApp> registeredApps = new EArrayList();
	private static EArrayList<EMCApp> incompatibleApps = new EArrayList();
	
	//convert list object into an array structure
	public static boolean registerApp(List<EMCApp> appsIn) {
		if (appsIn != null && appsIn.size() > 0) {
			Object[] appsO = appsIn.toArray();
			EMCApp[] apps = new EMCApp[appsO.length];
			for (int i = 0; i < appsO.length; i++) {
				if (appsO[i] instanceof EMCApp) {
					apps[i] = (EMCApp) appsO[i];
				}
			}
			return registerApp(apps);
		}
		return false;
	}
	
	//convert list object into an array structure
	public static boolean unregisterApp(List<EMCApp> appsIn) {
		if (appsIn != null && appsIn.size() > 0) {
			Object[] appsO = appsIn.toArray();
			EMCApp[] apps = new EMCApp[appsO.length];
			for (int i = 0; i < appsO.length; i++) {
				if (appsO[i] instanceof EMCApp) {
					apps[i] = (EMCApp) appsO[i];
				}
			}
			return unregisterApp(apps);
		}
		return false;
	}
	
	//attempt to register a variable amount of apps
	//only one instance of an app may be registered at any point
	public static boolean registerApp(EMCApp... appsIn) {
		EArrayList<EMCApp> appsToRegister = new EArrayList();
		
		for (EMCApp m : appsIn) {
			if (!isAppRegistered(m)) { appsToRegister.add(m); }
		}
		
		if (appsToRegister.size() > 0) {
			for (EMCApp m : appsToRegister) {
				if (m != null) {
					if (m.isIncompatible()) {
						EArrayList<EMCApp> incompats = new EArrayList();
						EnhancedMC.log(Level.INFO, "Unable to fully register EMC App: " + m.getName() + " is incompatible with one or more EMC Apps.");
						incompatibleApps.add(m);
					}
					else {
						EnhancedMC.log(Level.INFO, "Registering EMC App: " + m.getName());
						registeredApps.add(m);
					}
					allApps.add(m);
				}
			}
			
			EArrayList<EMCApp> all = new EArrayList(allApps);
			EArrayList<EMCApp> sorted = new EArrayList();
		
			//core first
			for (EMCApp a : all) {
				if (a.getAppType() == AppType.CORE) { sorted.add(a); break; }
			}
			all.removeAll(sorted);
			
			//disableable 2nd
			for (EMCApp a : all) {
				if (!a.isDisableable()) { sorted.add(a); }
			}
			all.removeAll(sorted);
			
			//alphabetical all else
			Collections.sort(all);
			sorted.addAll(all);
			
			allApps = new EArrayList(sorted);
			
			return true;
		}
		return false;
	}
	
	//attempt to unregister a variable amount of apps
	public static boolean unregisterApp(EMCApp... appsIn) {
		EArrayList<EMCApp> appsToUnregister = new EArrayList();
		
		for (EMCApp m : appsIn) {
			if (isAppRegistered(m)) { appsToUnregister.add(m); }
		}
		
		if (appsToUnregister.size() > 0) {
			for (EMCApp m : appsToUnregister) {
				if (m != null) {
					if (m.isIncompatible()) {
						EnhancedMC.log(Level.INFO, "Removing incompatible EMC App: " + m.getName());
						incompatibleApps.remove(m);
					}
					else {
						EnhancedMC.log(Level.INFO, "Unregistering EMC App: " + m.getName());
						registeredApps.remove(m);
					}
					allApps.remove(m);
				}
			}
			return true;
		}
		return false;
	}
	
	//returns a submod object from a given apptype if it is currently registered
	public static EMCApp getApp(AppType typeIn) {
		for (EMCApp m : allApps) {
			if (m.getAppType() != null) {
				if (m.getAppType().equals(typeIn)) { return m; }
			}
		}
		return null;
	}
	
	public static EMCApp getApp(String appName) {
		for (EMCApp m : allApps) { if (m.getName().equals(appName)) { return m; } }
		return null;
	}
	
	public static EMCApp getAppByAlias(String aliasIn) {
		if (aliasIn != null && !aliasIn.isEmpty()) {
			aliasIn = aliasIn.toLowerCase();
			for (EMCApp m : allApps) {
				for (String s : m.getNameAliases()) {
					if (s.equals(aliasIn)) { return m; }
				}
			}
		}
		return null;
	}
	
	//returns a file object with the expected location for the specified app
	public static File getAppConfigBaseFileLocation(AppType typeIn) {
		return new File("EnhancedMC" + "/" + typeIn.appName);
	}
	
	//returns a file object with the expected location for the specified app mod's name
	public static File getAppConfigBaseFileLocation(String nameIn) {
		return new File("EnhancedMC" + "/" + nameIn);
	}
	
	//returns true if a app of specified name is currently registered
	public static boolean isAppRegistered(EMCApp appIn) { return isAppRegistered(appIn.getName()); }
	public static boolean isAppRegistered(AppType typeIn) { return isAppRegistered(typeIn.appName); }
	public static boolean isAppRegistered(String nameIn) {
		for (EMCApp m : registeredApps) {
			if (m.getName().equals(nameIn)) { return true; }
		}
		return false;
	}
	
	public static boolean isAppRegEn(EMCApp appIn) { return isAppRegEn(appIn.getName()); }
	public static boolean isAppRegEn(AppType typeIn) { return isAppRegEn(typeIn.appName); }
	public static boolean isAppRegEn(String nameIn) {
		for (EMCApp m : registeredApps) {
			if (m.getName().equals(nameIn) && m.isEnabled()) { return true; }
		}
		return false;
	}
	
	//returns a copy of the current list of registered apps at the called instant
	public static EArrayList<EMCApp> getRegisteredAppList() {return new EArrayList(registeredApps); }
	//returns a copy of the current incompatible apps
	public static EArrayList<EMCApp> getIncompatibleAppList() { return new EArrayList(incompatibleApps); }
	//returns the current list of all apps currently loaded
	public static EArrayList<EMCApp> getAppsList() { return allApps; }
	
	public static EArrayList<String> getAppNames() {
		EArrayList<String> names = new EArrayList();
		for (EMCApp m : allApps) {
			names.add(m.getName());
		}
		return names;
	}
	
	//returns a list of currently enabled apps
	public static EArrayList<EMCApp> getEnabledAppsList() {
		EArrayList<EMCApp> enabledApps = new EArrayList();
		registeredApps.forEach((m) -> { if (m.isEnabled()) { enabledApps.add(m); } });
		return enabledApps;
	}
	
	//returns a list of currently disabled apps
	public static EArrayList<EMCApp> getDisabledAppsList() {
		EArrayList<EMCApp> disabledApps = new EArrayList();
		registeredApps.forEach((m) -> { if (!m.isEnabled()) { disabledApps.add(m); } });
		return disabledApps;
	}
	
	//returns a list of app names which are direct dependencies of the given app
	public static EArrayList<String> getAllAppDependencies(EMCApp appIn) {
		try {
			EArrayList<String> allDependencies = new EArrayList(appIn.getDependencies().getObjects());
			EArrayList<String> withDep = new EArrayList();
			EArrayList<String> workList = new EArrayList();
			
			allDependencies.forEach(t -> { EMCApp m = getApp(t); if (!m.getDependencies().isEmpty()) { withDep.add(m.getName()); } });
			withDep.forEach(t -> { getApp(t).getDependencies().getObjects().forEach(d -> { workList.add(d); }); });
			
			while (true) {
				if (!workList.isEmpty()) {
					allDependencies.addAll(workList);
					withDep.clear();
					workList.forEach(t -> {
						EMCApp m = getApp(t); if (m != null && !m.getDependencies().isEmpty()) { withDep.add(m.getName()); }
					});
					workList.clear();
					withDep.forEach(t -> { getApp(t).getDependencies().forEach(d -> { workList.add(d.getObject()); }); });
				} else { break; }
			}
			return allDependencies;
		} catch (Exception e) { e.printStackTrace(); }
		return new EArrayList();
	}
	
	//returns a list of app names which are dependant on the given app
	public static EArrayList<String> getAllDependantsOfApp(EMCApp appIn) {
		try {
			EArrayList<String> dependants = new EArrayList();
			EArrayList<String> workList = new EArrayList();
			registeredApps.forEach(m -> {
				if (m.getDependencies() != null) {
					m.getDependencies().forEach(t -> { if (t.getObject().equals(appIn.getName())) { dependants.add(m.getName()); } });
				}
			});
			for (EMCApp m : registeredApps) {
				for (String t : m.getDependencies().getObjects()) {
					for (String s : dependants) {
						if (t.equals(s)) { if (!dependants.contains(m.getName())) { workList.add(m.getName()); } }
					}
				}
			}
			dependants.addAll(workList);
			return dependants;
		} catch (Exception e) { e.printStackTrace(); }
		return new EArrayList();
	}
	
	//returns a list of app names which have a dependency to the given app
	public static EArrayList<String> getAllAppsWithDependency(AppType typeIn) { return getAllAppsWithDependency(typeIn.appName); }
	public static EArrayList<String> getAllAppsWithDependency(String appNameIn) {
		EArrayList<String> mods = new EArrayList();
		getRegisteredAppList().forEach(m -> { if (m.getDependencies().contains(appNameIn)) { mods.add(m.getName()); } });
		return mods;
	}
	
	//returns a list of app names which are currently enabled that have a dependency to the given app
	public static EArrayList<String> getAllEnabledAppsWithDependency(AppType typeIn) { return getAllEnabledAppsWithDependency(typeIn.appName); }
	public static EArrayList<String> getAllEnabledAppsWithDependency(String appNameIn) {
		EArrayList<String> mods = new EArrayList();
		getEnabledAppsList().forEach(m -> { if (m.getDependencies().contains(appNameIn)) { mods.add(m.getName()); } });
		return mods;
	}
	
	//returns a list of app names which are currently disabled that have a dependency to the given app
	public static EArrayList<String> getAllDisabledAppsWithDependency(AppType typeIn) { return getAllDisabledAppsWithDependency(typeIn.appName); }
	public static EArrayList<String> getAllDisabledAppsWithDependency(String appNameIn) {
		EArrayList<String> mods = new EArrayList();
		getDisabledAppsList().forEach(m -> { if (m.getDependencies().contains(appNameIn)) { mods.add(m.getName()); } });
		return mods;
	}
	
	//returns a list of all gui classes across all registered apps
	public static EArrayList<Class> getAllGuiClasses() {
		EArrayList<Class> guis = new EArrayList();
		for (EMCApp m : getRegisteredAppList()) {
			for (IWindowParent g : m.getGuis()) { if (g != null) { guis.add(g.getClass()); } }
		}
		return guis;
	}
	
	public static Class getGuiClassByAlias(String aliasIn) {
		if (aliasIn != null && !aliasIn.isEmpty()) {
			for (EMCApp m : getRegisteredAppList()) {
				for (IWindowParent g : m.getGuis()) {
					for (String a : g.getAliases()) {
						if (a.equals(aliasIn)) { return g.getClass(); }
					}
				}
			}
		}
		return null;
	}
	
	public static StorageBoxHolder<EMCApp, String> getAppImcompatibility(EMCApp appIn) {
		StorageBoxHolder<EMCApp, String> mods = new StorageBoxHolder();
		for (StorageBox<String, String> box : appIn.getDependencies()) {
			EMCApp foundMod = getApp(box.getObject());
			if (foundMod != null) {
				if (!foundMod.getVersion().equals(box.getValue())) { mods.add(foundMod, box.getValue()); }
				else if (foundMod.isIncompatible()) { mods.add(foundMod, box.getValue()); }
			}
		}
		return mods;
	}
}
