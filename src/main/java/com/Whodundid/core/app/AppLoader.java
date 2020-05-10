package com.Whodundid.core.app;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.Level;

public class AppLoader {
	
	public static boolean reloadApp(EMCApp appIn) { return reloadApp(null, appIn, false); }
	public static boolean reloadApp(ETerminal termIn, EMCApp appIn, boolean showInfo) {
		if (appIn != null) {
			
			if (showInfo && termIn != null) { termIn.writeln("Reloading app: " + appIn.getName() + ".", EColors.seafoam); }
			
			//unregister it before continuing
			RegisteredApps.unregisterApp(appIn);
			
			if (appIn != null) {
				//process app
				try {
					//check for incompatibility
					boolean incompatible = false;
					
					int numDeps = appIn.getDependencies().size();
					boolean incompatDep = false;
					int foundDepMods = 0;
					int matchingVers = 0;
					
					for (StorageBox<String, String> box : appIn.getDependencies()) {
						String appName = box.getObject();
						String appVer = box.getValue();
						
						EMCApp dep = null;
						for (EMCApp app : RegisteredApps.getAppsList()) {
							if (app.getName().equals(appName)) { dep = app; break; }
						}
						
						if (dep != null) {
							foundDepMods += 1;
							if (appVer.equals(dep.getVersion())) { matchingVers += 1; }
						}
					}
					
					if (foundDepMods != numDeps || matchingVers != numDeps) { incompatible = true; }
					
					//set incompatibility state
					appIn.setIncompatible(incompatible);
				}
				catch (Exception e) {
					if (showInfo && termIn != null) { termIn.writeln("Error trying to read app: " + appIn.getName() + "! Ignoring...", EColors.red); }
					EnhancedMC.error("Error trying to read app: " + appIn.getName() + "! Ignoring...");
					e.printStackTrace();
				}
				
				//check if the processed mod was found incompatible with the core
				for (StorageBox<String, String> box : appIn.getDependencies()) {
					
					EMCApp dep = null;
					for (EMCApp app : RegisteredApps.getAppsList()) {
						if (app.getName().equals(box.getObject())) { dep = app; break; }
					}
					
					if (dep != null) {
						//set the mod incompat if one of it's dependencies was incompat too
						if (dep.isIncompatible()) {
							EnhancedMC.error("WARNING: EMC App '" + appIn.getName() + "' is incompatible!...");
							if (showInfo && termIn != null) { termIn.error("WARNING: EMC App '" + appIn.getName() + "' is incompatible!..."); }
							appIn.setIncompatible(true);
						}
					}
				}
				
				if (appIn.isIncompatible()) { EnhancedMC.error("Error: The app: " + appIn.getName() + " is incompatible. Registration halted."); }
				else {
					if (showInfo && termIn != null) { termIn.info("Registering App: " + appIn.getName()); }
					RegisteredApps.registerApp(appIn);
					
					//config
					try {
						if (appIn.hasConfig()) { appIn.getConfig().loadAllConfigs(); appIn.getConfig().saveAllConfigs(); }
					}
					catch (Exception e) {
						if (showInfo && termIn != null) { termIn.writeln("Error tying to load EMCApp: " + appIn.getName() + " config!", EColors.red); }
						EnhancedMC.log(Level.INFO, "Error tying to load EMCApp: " + appIn.getName() + " config!");
						e.printStackTrace();
					}
					
					//init
					try {
						appIn.onPostInit(new FMLPostInitializationEvent());
						if (!appIn.isDisableable()) { appIn.setEnabled(true); }
					}
					catch (Exception q) {
						if (showInfo && termIn != null) { termIn.writeln("Error trying to run postInit on EMCApp: " + appIn.getName() + "!", EColors.red); }
						EnhancedMC.log(Level.INFO, "Error trying to run postInit on EMCApp: " + appIn.getName() + "!");
						q.printStackTrace();
					}
					
					//resources
					try {
						appIn.registerResources();
					}
					catch (Exception q) {
						if (showInfo && termIn != null) { termIn.writeln("Error trying to register resources on EMCApp: " + appIn.getName() + "!", EColors.red); }
						EnhancedMC.log(Level.INFO, "Error trying to register resources on EMCApp: " + appIn.getName() + "!");
						q.printStackTrace();
					}
					
					//reload gloabl
					try {
						//attempt to load the EMCApp settings file
						AppSettings.loadConfig();
					}
					catch (Exception q) {
						if (showInfo && termIn != null) { termIn.writeln("Error trying to load global config!", EColors.red); }
						EnhancedMC.log(Level.INFO, "Error trying to load global config!");
						q.printStackTrace();
					}
					
					if (showInfo && termIn != null) { termIn.writeln("EMCApp: " + appIn.getName() + " successfully reloaded!", EColors.green); }
					
					return true;
				}
			} //false
			
		}
		
		return false;
	}
	
	public static void loadApps() { loadApps(null, false); }
	public static void loadApps(ETerminal termIn, boolean showInfo) {
		
		EArrayList<EMCApp> foundApps = new EArrayList();
		EArrayList<EMCApp> coreCheck = new EArrayList();
		EArrayList<EMCApp> depCheck = new EArrayList();
		foundApps.add(EnhancedMC.appInstance);
		
		//find any EMCApps in forge loaded mods
		for (ModContainer c : Loader.instance().getModList()) {
			Object m = c.getMod();
			if (m instanceof EMCApp) {
				c.getMetadata().parent = EnhancedMC.MODID; //assign the app as a child of EMC
				foundApps.add((EMCApp) m);
				if (showInfo && termIn != null) { termIn.writeln("Found: " + EnumChatFormatting.AQUA + ((EMCApp) m).getName(), EColors.seafoam); }
			}
		}
		
		//process found apps
		for (EMCApp m : foundApps) {
			try {
				//check for incompatibility
				boolean incompatible = false;
				
				int numDeps = m.getDependencies().size();
				boolean incompatDep = false;
				int foundDepMods = 0;
				int matchingVers = 0;
				
				for (StorageBox<String, String> box : m.getDependencies()) {
					String appName = box.getObject();
					String appVer = box.getValue();
					
					EMCApp dep = getApp(appName, foundApps);
					if (dep != null) {
						foundDepMods += 1;
						if (appVer.equals(dep.getVersion())) { matchingVers += 1; }
					}
				}
				
				if (foundDepMods != numDeps || matchingVers != numDeps) { incompatible = true; }
				
				//set incompatibility state
				m.setIncompatible(incompatible);
				
				//register the subMod into core
				coreCheck.add(m);
			}
			catch (Exception q) {
				if (showInfo && termIn != null) { termIn.writeln("Error trying to read app: " + m + "! Ignoring...", EColors.red); }
				EnhancedMC.error("Error trying to read app: " + m + "! Ignoring...");
				q.printStackTrace();
			}
		}
		
		//check processed mods for subMod incompatibilities
		for (EMCApp m : coreCheck) {
			//check if the processed mod was found incompatible with the core
			for (StorageBox<String, String> box : m.getDependencies()) {
				EMCApp dep = getApp(box.getObject(), coreCheck);
				if (dep != null) {
					//set the mod incompat if one of it's dependencies was incompat too
					if (dep.isIncompatible()) {
						EnhancedMC.error("WARNING: EMC App '" + m.getName() + "' is incompatible!...");
						m.setIncompatible(true);
					}
				}
			}
			
			depCheck.add(m);
		}
		
		//finally register each processed app
		depCheck.stream().filter(a -> a.shouldLoad).forEach(a -> RegisteredApps.registerApp(a));
		
		//attempt to load each of the found EMCApp's config files (if they exist)
		EnhancedMC.info("Loading EMC App Configs...");
		for (EMCApp m : RegisteredApps.getRegisteredAppList()) {
			try {
				if (m.hasConfig()) { m.getConfig().loadAllConfigs(); m.getConfig().saveAllConfigs(); }
			}
			catch (NullPointerException q) {
				if (showInfo && termIn != null) { termIn.writeln("Error tying to load EMCApp: " + m + " config!", EColors.red); }
				EnhancedMC.log(Level.INFO, "Error tying to load EMCApp: " + m + " config!");
				q.printStackTrace();
			}
		}
		
		//send postInit update to each loaded EMCApp
		EnhancedMC.info("Initializing EMC Apps...");
		for (EMCApp m : RegisteredApps.getRegisteredAppList()) {
			try {
				m.onPostInit(new FMLPostInitializationEvent());
				if (!m.isDisableable()) { m.setEnabled(true); }
			}
			catch (Exception q) {
				if (showInfo && termIn != null) { termIn.writeln("Error trying to run postInit on EMCApp: " + m + "!", EColors.red); }
				EnhancedMC.log(Level.INFO, "Error trying to run postInit on EMCApp: " + m + "!");
				q.printStackTrace();
			}
		}
		
		//send registerResources event to each loaded EMCApp
		EnhancedMC.info("Registering EMC App Resources...");
		for (EMCApp m : RegisteredApps.getRegisteredAppList()) {
			try {
				m.registerResources();
			}
			catch (Exception q) {
				if (showInfo && termIn != null) { termIn.writeln("Error trying to register resources on EMCApp: " + m + "!", EColors.red); }
				EnhancedMC.log(Level.INFO, "Error trying to register resources on EMCApp: " + m + "!");
				q.printStackTrace();
			}
		}
		
		EnhancedMC.info("Loading EMC Global Settings Config...");
		try {
			//attempt to load the EMCApp settings file
			AppSettings.loadConfig();
		}
		catch (Exception q) {
			if (showInfo && termIn != null) { termIn.writeln("Error trying to load global config!", EColors.red); }
			EnhancedMC.log(Level.INFO, "Error trying to load global config!");
			q.printStackTrace();
		}
	}
	
	//--------------------------
	//AppLoader Internal Methods
	//--------------------------
	
	private static EMCApp getApp(String appNameIn, EArrayList<EMCApp> checkList) {
		for (EMCApp m : checkList) { if (m.getName().equals(appNameIn)) { return m; } }
		return null;
	}

}
