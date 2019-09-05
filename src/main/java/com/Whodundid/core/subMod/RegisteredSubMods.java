package com.Whodundid.core.subMod;

import net.minecraft.client.gui.GuiScreen;
import org.apache.logging.log4j.Level;
import java.io.File;
import java.util.List;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Dec 28, 2018
//Last edited: Feb 17, 2019
//Edit note: changing isModAlreadyRegistered -> isModRegistered. Added ability to check if SubModType is registered.
//First Added: Oct 16, 2018
//Author: Hunter Bragg

public class RegisteredSubMods {
	
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
						EnhancedMC.log(Level.INFO, "Unable to fully register SubMod: " + SubModType.getModName(m.getModType()) + " is incompatible with other loaded SubMods.");
						incompatibleMods.add(m);
					}
					else {
						EnhancedMC.log(Level.INFO, "Registering SubMod: " + SubModType.getModName(m.getModType()));
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
						EnhancedMC.log(Level.INFO, "Removing incompatible SubMod: " + SubModType.getModName(m.getModType()));
						incompatibleMods.remove(m);
					}
					else {
						EnhancedMC.log(Level.INFO, "Unregistering SubMod: " + SubModType.getModName(m.getModType()));
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
		for (SubMod m : registeredMods) { if (m.getModType().equals(typeIn)) { return m; } }
		return null;
	}
	
	public static SubMod getMod(String modName) {
		for (SubMod m : registeredMods) { if (m.getName().equals(modName)) { return m; } }
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
	public static EArrayList<SubModType> getAllModDependencies(SubMod modIn) {
		EArrayList<SubModType> allDependencies = new EArrayList(modIn.getDependencies().getObjects());
		EArrayList<SubModType> withDep = new EArrayList();
		EArrayList<SubModType> workList = new EArrayList();
		
		allDependencies.forEach(t -> { SubMod m = getMod(t); if (!m.getDependencies().isEmpty()) { withDep.add(m.getModType()); } });
		withDep.forEach(t -> { getMod(t).getDependencies().getObjects().forEach(d -> { workList.add(d); }); });
		
		while (true) {
			if (!workList.isEmpty()) {
				allDependencies.addAll(workList);
				withDep.clear();
				workList.forEach(t -> { SubMod m = getMod(t); if (!m.getDependencies().isEmpty()) { withDep.add(m.getModType()); } });
				workList.clear();
				withDep.forEach(t -> { getMod(t).getDependencies().forEach(d -> { workList.add(d.getObject()); }); });
			} else { break; }
		}
		return allDependencies;
	}
	
	//returns a list of submodtypes which are dependant on the given submod
	public static EArrayList<SubModType> getAllDependantsOfMod(SubMod modIn) {
		EArrayList<SubModType> dependants = new EArrayList();
		EArrayList<SubModType> workList = new EArrayList();
		registeredMods.forEach(m -> { m.getDependencies().forEach(t -> { if (t.getObject().equals(modIn.getModType())) { dependants.add(m.getModType()); } }); });
		for (SubMod m : registeredMods) {
			for (SubModType t : m.getDependencies().getObjects()) {
				for (SubModType d : dependants) {
					if (t.equals(d)) { if (!dependants.contains(m.getModType())) { workList.add(m.getModType()); } }
				}
			}
		}
		dependants.addAll(workList);
		return dependants;
	}
	
	//returns a list of submodtypes which have a dependency to the given mod
	public static EArrayList<SubModType> getAllModsWithDepenency(SubModType typeIn) {
		EArrayList<SubModType> mods = new EArrayList();
		getRegisteredModsList().forEach(m -> { if (m.getDependencies().contains(typeIn)) { mods.add(m.getModType()); } });
		return mods;
	}
	
	//returns a list of submodtypes which are currently enabled that have a dependency to the given mod
	public static EArrayList<SubModType> getAllEnabledModsWithDepenency(SubModType typeIn) {
		EArrayList<SubModType> mods = new EArrayList();
		getEnabledModsList().forEach(m -> { if (m.getDependencies().contains(typeIn)) { mods.add(m.getModType()); } });
		return mods;
	}
	
	//returns a list of submodtypes which are currently disabled that have a dependency to the given mod
	public static EArrayList<SubModType> getAllDisabledModsWithDepenency(SubModType typeIn) {
		EArrayList<SubModType> mods = new EArrayList();
		getDisabledModsList().forEach(m -> { if (m.getDependencies().contains(typeIn)) { mods.add(m.getModType()); } });
		return mods;
	}
	
	//returns a list of all gui classes across all registered submods
	public static EArrayList<Class> getAllGuiClasses() {
		EArrayList<Class> guis = new EArrayList();
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) {
			for (GuiScreen g : m.getGuis()) { if (g != null) { guis.add(g.getClass()); } }
		}
		return guis;
	}
}
