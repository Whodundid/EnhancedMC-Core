package com.Whodundid.core.subMod;

import org.apache.logging.log4j.Level;
import java.io.File;
import java.util.List;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;

//Dec 28, 2018
//Last edited: Feb 17, 2019
//Edit note: changing isModAlreadyRegistered -> isModRegistered. Added ability to check if SubModType is registered.
//First Added: Oct 16, 2018
//Author: Hunter Bragg

public final class RegisteredSubMods {
	
	private static EArrayList<SubMod> allMods = new EArrayList();
	private static EArrayList<SubMod> registeredMods = new EArrayList();
	private static EArrayList<SubMod> incompatibleMods = new EArrayList();
	
	//convert list object into an array structure
	public static boolean registerSubMod(List<SubMod> modsIn) {
		if (modsIn != null && modsIn.size() > 0) {
			Object[] modsO = modsIn.toArray();
			SubMod[] mods = new SubMod[modsO.length];
			for (int i = 0; i < modsO.length; i++) {
				if (modsO[i] instanceof SubMod) {
					mods[i] = (SubMod) modsO[i];
				}
			}
			return registerSubMod(mods);
		}
		return false;
	}
	
	//convert list object into an array structure
	public static boolean unregisterSubMod(List<SubMod> modsIn) {
		if (modsIn != null && modsIn.size() > 0) {
			Object[] modsO = modsIn.toArray();
			SubMod[] mods = new SubMod[modsO.length];
			for (int i = 0; i < modsO.length; i++) {
				if (modsO[i] instanceof SubMod) {
					mods[i] = (SubMod) modsO[i];
				}
			}
			return unregisterSubMod(mods);
		}
		return false;
	}
	
	//attempt to register a variable amount of submods
	//only one instance of a submod may be registered at any point
	public static boolean registerSubMod(SubMod... modsIn) {
		EArrayList<SubMod> modsToRegister = new EArrayList();
		
		for (SubMod m : modsIn) {
			if (!isModRegistered(m)) { modsToRegister.add(m); }
		}
		
		if (modsToRegister.size() > 0) {
			for (SubMod m : modsToRegister) {
				if (m != null) {
					if (m.isIncompatible()) {
						EArrayList<SubMod> incompats = new EArrayList();
						EnhancedMC.log(Level.INFO, "Unable to fully register SubMod: " + m.getName() + " is incompatible with one or more EMC SubMods.");
						incompatibleMods.add(m);
					}
					else {
						EnhancedMC.log(Level.INFO, "Registering SubMod: " + m.getName());
						registeredMods.add(m);
					}
					allMods.add(m);
				}
			}
			return true;
		}
		return false;
	}
	
	//attempt to unregister a variable amount of submods
	public static boolean unregisterSubMod(SubMod... modsIn) {
		EArrayList<SubMod> modsToUnregister = new EArrayList();
		
		for (SubMod m : modsIn) {
			if (isModRegistered(m)) { modsToUnregister.add(m); }
		}
		
		if (modsToUnregister.size() > 0) {
			for (SubMod m : modsToUnregister) {
				if (m != null) {
					if (m.isIncompatible()) {
						EnhancedMC.log(Level.INFO, "Removing incompatible SubMod: " + m.getName());
						incompatibleMods.remove(m);
					}
					else {
						EnhancedMC.log(Level.INFO, "Unregistering SubMod: " + m.getName());
						registeredMods.remove(m);
					}
					allMods.remove(m);
				}
			}
			return true;
		}
		return false;
	}
	
	//returns a submod object from a given submodtype if it is currently registered
	public static SubMod getMod(SubModType typeIn) {
		for (SubMod m : allMods) { if (m.getModType().equals(typeIn)) { return m; } }
		return null;
	}
	
	public static SubMod getMod(String modName) {
		for (SubMod m : allMods) { if (m.getName().equals(modName)) { return m; } }
		return null;
	}
	
	public static SubMod getModByAlias(String aliasIn) {
		if (aliasIn != null && !aliasIn.isEmpty()) {
			aliasIn = aliasIn.toLowerCase();
			for (SubMod m : allMods) {
				for (String s : m.getNameAliases()) {
					if (s.equals(aliasIn)) { return m; }
				}
			}
		}
		return null;
	}
	
	//returns a file object with the expected location for the specified submod
	public static File getModConfigBaseFileLocation(SubModType typeIn) {
		return new File("EnhancedMC" + "/" + typeIn.modName);
	}
	
	//returns a file object with the expected location for the specified submod mod's name
	public static File getModConfigBaseFileLocation(String nameIn) {
		return new File("EnhancedMC" + "/" + nameIn);
	}
	
	//returns true if a submod of specified name is currently registered
	public static boolean isModRegistered(SubMod modIn) { return isModRegistered(modIn.getName()); }
	public static boolean isModRegistered(SubModType typeIn) { return isModRegistered(typeIn.modName); }
	public static boolean isModRegistered(String nameIn) {
		for (SubMod m : registeredMods) {
			if (m.getName().equals(nameIn)) { return true; }
		}
		return false;
	}
	
	public static boolean isModRegEn(SubMod modIn) { return isModRegEn(modIn.getName()); }
	public static boolean isModRegEn(SubModType typeIn) { return isModRegEn(typeIn.modName); }
	public static boolean isModRegEn(String nameIn) {
		for (SubMod m : registeredMods) {
			if (m.getName().equals(nameIn) && m.isEnabled()) { return true; }
		}
		return false;
	}
	
	//returns a copy of the current list of registered mods at the called instant
	public static EArrayList<SubMod> getRegisteredModsList() {return new EArrayList(registeredMods); }
	//returns a copy of the current incompatible subMods
	public static EArrayList<SubMod> getIncompatibleModsList() { return new EArrayList(incompatibleMods); }
	//returns the current list of all subMods currently loaded
	public static EArrayList<SubMod> getModsList() { return allMods; }
	
	//returns a list of currently enabled submods
	public static EArrayList<SubMod> getEnabledModsList() {
		EArrayList<SubMod> enabledMods = new EArrayList();
		registeredMods.forEach((m) -> { if (m.isEnabled()) { enabledMods.add(m); } });
		return enabledMods;
	}
	
	//returns a list of currently disabled submods
	public static EArrayList<SubMod> getDisabledModsList() {
		EArrayList<SubMod> disabledMods = new EArrayList();
		registeredMods.forEach((m) -> { if (!m.isEnabled()) { disabledMods.add(m); } });
		return disabledMods;
	}
	
	//returns a list of submodtypes which are direct dependencies of the given submod
	public static EArrayList<String> getAllModDependencies(SubMod modIn) {
		try {
			EArrayList<String> allDependencies = new EArrayList(modIn.getDependencies().getObjects());
			EArrayList<String> withDep = new EArrayList();
			EArrayList<String> workList = new EArrayList();
			
			allDependencies.forEach(t -> { SubMod m = getMod(t); if (!m.getDependencies().isEmpty()) { withDep.add(m.getName()); } });
			withDep.forEach(t -> { getMod(t).getDependencies().getObjects().forEach(d -> { workList.add(d); }); });
			
			while (true) {
				if (!workList.isEmpty()) {
					allDependencies.addAll(workList);
					withDep.clear();
					workList.forEach(t -> {
						SubMod m = getMod(t); if (m != null && !m.getDependencies().isEmpty()) { withDep.add(m.getName()); }
					});
					workList.clear();
					withDep.forEach(t -> { getMod(t).getDependencies().forEach(d -> { workList.add(d.getObject()); }); });
				} else { break; }
			}
			return allDependencies;
		} catch (Exception e) { e.printStackTrace(); }
		return new EArrayList();
	}
	
	//returns a list of submod names which are dependant on the given submod
	public static EArrayList<String> getAllDependantsOfMod(SubMod modIn) {
		try {
			EArrayList<String> dependants = new EArrayList();
			EArrayList<String> workList = new EArrayList();
			registeredMods.forEach(m -> {
				if (m.getDependencies() != null) {
					m.getDependencies().forEach(t -> { if (t.getObject().equals(modIn.getName())) { dependants.add(m.getName()); } });
				}
			});
			for (SubMod m : registeredMods) {
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
	
	//returns a list of submod names which have a dependency to the given mod
	public static EArrayList<String> getAllModsWithDependency(SubModType typeIn) { return getAllModsWithDependency(typeIn.modName); }
	public static EArrayList<String> getAllModsWithDependency(String modNameIn) {
		EArrayList<String> mods = new EArrayList();
		getRegisteredModsList().forEach(m -> { if (m.getDependencies().contains(modNameIn)) { mods.add(m.getName()); } });
		return mods;
	}
	
	//returns a list of submod names which are currently enabled that have a dependency to the given mod
	public static EArrayList<String> getAllEnabledModsWithDependency(SubModType typeIn) { return getAllEnabledModsWithDependency(typeIn.modName); }
	public static EArrayList<String> getAllEnabledModsWithDependency(String modNameIn) {
		EArrayList<String> mods = new EArrayList();
		getEnabledModsList().forEach(m -> { if (m.getDependencies().contains(modNameIn)) { mods.add(m.getName()); } });
		return mods;
	}
	
	//returns a list of submod names which are currently disabled that have a dependency to the given mod
	public static EArrayList<String> getAllDisabledModsWithDependency(SubModType typeIn) { return getAllDisabledModsWithDependency(typeIn.modName); }
	public static EArrayList<String> getAllDisabledModsWithDependency(String modNameIn) {
		EArrayList<String> mods = new EArrayList();
		getDisabledModsList().forEach(m -> { if (m.getDependencies().contains(modNameIn)) { mods.add(m.getName()); } });
		return mods;
	}
	
	//returns a list of all gui classes across all registered submods
	public static EArrayList<Class> getAllGuiClasses() {
		EArrayList<Class> guis = new EArrayList();
		for (SubMod m : getRegisteredModsList()) {
			for (IWindowParent g : m.getGuis()) { if (g != null) { guis.add(g.getClass()); } }
		}
		return guis;
	}
	
	public static Class getGuiClassByAlias(String aliasIn) {
		if (aliasIn != null && !aliasIn.isEmpty()) {
			for (SubMod m : getRegisteredModsList()) {
				for (IWindowParent g : m.getGuis()) {
					for (String a : g.getAliases()) {
						if (a.equals(aliasIn)) { return g.getClass(); }
					}
				}
			}
		}
		return null;
	}
	
	public static StorageBoxHolder<SubMod, String> getModImcompatibility(SubMod modIn) {
		StorageBoxHolder<SubMod, String> mods = new StorageBoxHolder();
		for (StorageBox<String, String> box : modIn.getDependencies()) {
			SubMod foundMod = getMod(box.getObject());
			if (foundMod != null) {
				if (!foundMod.getVersion().equals(box.getValue())) { mods.add(foundMod, box.getValue()); }
				else if (foundMod.isIncompatible()) { mods.add(foundMod, box.getValue()); }
			}
		}
		return mods;
	}
}
