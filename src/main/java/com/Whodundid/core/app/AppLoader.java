package com.Whodundid.core.app;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreEvents.emcEvents.ReloadingAppEvent;
import com.Whodundid.core.notifications.util.NotificationType;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.Level;

public class AppLoader {
	
	public static boolean reloadApp(EMCApp appIn) { return reloadApp(null, appIn, false); }
	public static boolean reloadApp(ETerminal termIn, EMCApp appIn, boolean showInfo) {
		if (appIn != null) {
			MinecraftForge.EVENT_BUS.post(new ReloadingAppEvent.Pre(appIn));
			boolean tin = showInfo && termIn != null;
			
			if (tin) { termIn.writeln("Reloading app: " + appIn.getName() + ".", EColors.seafoam); }
			
			//unregister it before continuing
			RegisteredApps.unregisterApp(appIn);
			
			//unregister the apps notifications
			try {
				EArrayList<NotificationType> notes = appIn.getNotifications();
				if (notes.isNotEmpty()) {
					if (tin) { termIn.writeln("Attempting to unregister notifications..", EColors.yellow); }
					for (NotificationType t : appIn.getNotifications()) {
						EnhancedMC.getNotificationHandler().unregisterNotificationType(t);
					}
					if (tin) { termIn.writeln("Notifications for App: " + appIn.getName() + " " + appIn.getVersion() + " unregistered!", EColors.lgreen); }
				}
			}
			catch (Exception e) {
				EnhancedMC.error("Error tyring to unregister notifications for: " + appIn.getName() + " " + appIn.getVersion() + "!", e);
				if (tin) { termIn.javaError("Error tyring to unregister notifications for: " + appIn.getName() + " " + appIn.getVersion() + "!"); }
			}
			
			//rebuild the app
			try {
				if (tin) { termIn.writeln("Attempting to rebuild app..", EColors.yellow); }
				appIn.rebuild();
				if (tin) { termIn.writeln("App: " + appIn.getName() + " " + appIn.getVersion() + " rebuilt!", EColors.lgreen); }
			}
			catch (Exception e) {
				EnhancedMC.error("Error tyring to rebuild app: " + appIn.getName() + " " + appIn.getVersion() + "!", e);
				if (tin) { termIn.javaError("Error tyring to rebuild app: " + appIn.getName() + " " + appIn.getVersion() + "!"); }
			}
			
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
					
					String[] appVerParts = appVer.split("[.]");
					int appMajorVer = -1;
					int appMinorVer = -1;
					int appBuildVer = -1;
					
					if (appVerParts.length <= 3) {
						if (appVerParts.length > 0) { appMajorVer = Integer.parseInt(appVerParts[0]); }
						if (appVerParts.length > 1) { appMinorVer = Integer.parseInt(appVerParts[1]); }
						if (appVerParts.length > 2) { appBuildVer = Integer.parseInt(appVerParts[2]); }
						
						if (appMajorVer != -1) {
							EMCApp dep = null;
							for (EMCApp app : RegisteredApps.getAppsList()) {
								if (app.getName().equals(appName)) { dep = app; break; }
							}
							
							if (dep != null) {
								foundDepMods += 1;
								
								String depVer = dep.getVersion();
								
								String[] depVerParts = depVer.split("[.]");
								int depMajorVer = -1;
								int depMinorVer = -1;
								int depBuildVer = -1;
								
								if (depVerParts.length > 0) { depMajorVer = Integer.parseInt(depVerParts[0]); }
								if (depVerParts.length > 1) { depMinorVer = Integer.parseInt(depVerParts[1]); }
								if (depVerParts.length > 2) { depBuildVer = Integer.parseInt(depVerParts[2]); }
								
								if (depMajorVer != -1) {
									//check major - minor - build versions
									if (dep.enforcesBuildVersion()) { 
										if (appVer.equals(depVer)) { matchingVers += 1; }
									}
									
									//check major version
									else if (dep.enforcesMajorVersion()) {
										if (appMajorVer == depMajorVer) { matchingVers += 1; }
									}
									
									//check major - minor versions
									else if (dep.enforcesMinorVersion()) {
										if (appMajorVer == depMajorVer && appMinorVer == depMinorVer) { matchingVers += 1; } 
									}
								}
							}
						} //if appMajorVer != null
					}
				}
				
				if (foundDepMods != numDeps || matchingVers != numDeps) { incompatible = true; }
				
				//set incompatibility state
				appIn.setIncompatible(incompatible);
			}
			catch (Exception e) {
				if (tin) { termIn.writeln("Error trying to read app: " + appIn.getName() + "! Ignoring...", EColors.red); }
				EnhancedMC.error("Error trying to read app: " + appIn.getName() + " " + appIn.getVersion() + " ! Ignoring...");
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
						if (tin) { termIn.error("WARNING: EMC App '" + appIn.getName() + " " + appIn.getVersion() + "' is incompatible!..."); }
						appIn.setIncompatible(true);
					}
				}
			}
			
			//keep the app even if it is incompatible
			RegisteredApps.registerApp(appIn);
			
			if (appIn.isIncompatible()) {
				EnhancedMC.error("Error: The app: " + appIn.getName() + " " + appIn.getVersion() + " is incompatible. Registration halted.");
				if (tin) {
					termIn.javaError("The app: " + appIn.getName() + " " + appIn.getVersion() + " is incompatible. Registration halted.");
				}
			}
			else {
				if (tin) { termIn.info("Registering App: " + appIn.getName() + " " + appIn.getVersion()); }
				
				//config
				try {
					if (appIn.hasConfig()) { appIn.getConfig().loadAllConfigs(); appIn.getConfig().saveAllConfigs(); }
				}
				catch (Exception e) {
					if (tin) { termIn.writeln("Error tying to load EMCApp: " + appIn.getName() + " " + appIn.getVersion() + " config!", EColors.red); }
					EnhancedMC.log(Level.INFO, "Error tying to load EMCApp: " + appIn.getName() + " " + appIn.getVersion() + " config!");
					e.printStackTrace();
				}
				
				//init
				try {
					appIn.onPostInit(new FMLPostInitializationEvent());
					if (!appIn.isDisableable()) { appIn.setEnabled(true); }
				}
				catch (Exception q) {
					if (tin) { termIn.writeln("Error trying to run postInit on EMCApp: " + appIn.getName() + " " + appIn.getVersion() + "!", EColors.red); }
					EnhancedMC.log(Level.INFO, "Error trying to run postInit on EMCApp: " + appIn.getName() + " " + appIn.getVersion() + "!");
					q.printStackTrace();
				}
				
				//resources
				try {
					appIn.registerResources();
				}
				catch (Exception q) {
					if (tin) { termIn.writeln("Error trying to register resources on EMCApp: " + appIn.getName() + " " + appIn.getVersion() + "!", EColors.red); }
					EnhancedMC.log(Level.INFO, "Error trying to register resources on EMCApp: " + appIn.getName() + " " + appIn.getVersion() + "!");
					q.printStackTrace();
				}
				
				//reload gloabl
				try {
					//attempt to load the EMCApp settings file
					AppSettings.loadConfig();
				}
				catch (Exception q) {
					if (tin) { termIn.writeln("Error trying to load global config!", EColors.red); }
					EnhancedMC.log(Level.INFO, "Error trying to load global config!");
					q.printStackTrace();
				}
				
				if (tin) { termIn.writeln("EMCApp: " + appIn.getName() + " " + appIn.getVersion() + " successfully reloaded!", EColors.green); }
				
				return true;
			}
			
			EnhancedMC.reloadAllWindows();
			
			MinecraftForge.EVENT_BUS.post(new ReloadingAppEvent.Post(appIn));
		}
		
		return false;
	}
	
	public static void loadApps() { loadApps(null, false); }
	public static void loadApps(ETerminal termIn, boolean showInfo) {
		
		boolean tin = showInfo && termIn != null;
		
		EArrayList<EMCApp> bundled = EnhancedMC.getBundledApps();
		EArrayList<EMCApp> foundApps = new EArrayList(EnhancedMC.appInstance);
		EArrayList<EMCApp> coreCheck = new EArrayList();
		EArrayList<EMCApp> depCheck = new EArrayList();
		foundApps.addAll(bundled);
		
		//find any EMCApps in forge loaded mods
		for (ModContainer c : Loader.instance().getModList()) {
			Object m = c.getMod();
			if (m instanceof EMCApp) {
				c.getMetadata().parent = EnhancedMC.MODID; //assign the app as a child of EMC
				EMCApp app = (EMCApp) m;
				
				if (app != null) {
					if (tin) { termIn.writeln("Found mod app: " + EnumChatFormatting.AQUA + ((EMCApp) m).getName(), EColors.seafoam); }
					
					boolean contains = false;
					for (EMCApp a : foundApps) {
						if (a != null && a.getName() != null) {
							if (a.getName().equals(app.getName())) { contains = true; break; }
						}
					}
					if (!contains) { foundApps.add(app); }
					else {
						if (tin) { termIn.writeln("Duplicate app found! App: " + app.getName() + " " + app.getVersion() + " is already present within Found Apps List!"); }
					}
				}
			}
		}
		
		EArrayList<EMCApp> builtApps = new EArrayList();
		StorageBoxHolder<EMCApp, Throwable> failedApps = new StorageBoxHolder();
		
		//attempt to build each found app
		for (EMCApp a : foundApps) {
			String name = "CAN'T READ NAME!";
			String version = "CAN'T READ VERSION!";
			try {
				name = a.getName();
				version = a.getVersion();
				a.build();
				builtApps.add(a);
			}
			catch (Throwable e) {
				EnhancedMC.error("Failed to build App: '" + name + "' " + version + "! Ignoring...");
				e.printStackTrace();
				failedApps.add(a, e);
			}
		}
		
		//handle the apps which failed to load
		for (StorageBox<EMCApp, Throwable> a : failedApps) {
			EnhancedMC.getApps().addToBrokenApps(a);
		}
		
		//process built apps
		for (EMCApp m : builtApps) {
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
					
					String[] appVerParts = appVer.split("[.]");
					int appMajorVer = -1;
					int appMinorVer = -1;
					int appBuildVer = -1;
					
					if (appVerParts.length <= 3) {
						if (appVerParts.length > 0) { appMajorVer = Integer.parseInt(appVerParts[0]); }
						if (appVerParts.length > 1) { appMinorVer = Integer.parseInt(appVerParts[1]); }
						if (appVerParts.length > 2) { appBuildVer = Integer.parseInt(appVerParts[2]); }
						
						if (appMajorVer != -1) {
							EMCApp dep = getApp(appName, foundApps);
							if (dep != null) {
								foundDepMods += 1;
								
								String depVer = dep.getVersion();
								
								String[] depVerParts = depVer.split("[.]");
								int depMajorVer = -1;
								int depMinorVer = -1;
								int depBuildVer = -1;
								
								if (depVerParts.length > 0) { depMajorVer = Integer.parseInt(depVerParts[0]); }
								if (depVerParts.length > 1) { depMinorVer = Integer.parseInt(depVerParts[1]); }
								if (depVerParts.length > 2) { depBuildVer = Integer.parseInt(depVerParts[2]); }
								
								if (depMajorVer != -1) {
									//check major - minor - build versions
									if (dep.enforcesBuildVersion()) { 
										if (appVer.equals(depVer)) { matchingVers += 1; }
									}
									
									//check major version
									else if (dep.enforcesMajorVersion()) {
										if (appMajorVer == depMajorVer) { matchingVers += 1; }
									}
									
									//check major - minor versions
									else if (dep.enforcesMinorVersion()) {
										if (appMajorVer == depMajorVer && appMinorVer == depMinorVer) { matchingVers += 1; } 
									}
								}
							} //dep != null
						}
					} //appVerParts <= 3
				}
				
				if (foundDepMods != numDeps || matchingVers != numDeps) { incompatible = true; }
				
				//set incompatibility state
				m.setIncompatible(incompatible);
				
				//register the subMod into core
				coreCheck.add(m);
			}
			catch (Exception q) {
				if (tin) { termIn.writeln("Error trying to read app: " + m + "! Ignoring...", EColors.red); }
				EnhancedMC.error("Error trying to read app: " + m + "! Ignoring...");
				q.printStackTrace();
			}
		}
		
		//check processed mods for EMCApp incompatibilities
		for (EMCApp m : coreCheck) {
			//check if the processed app was found incompatible with the core
			for (StorageBox<String, String> box : m.getDependencies()) {
				EMCApp dep = getApp(box.getObject(), coreCheck);
				if (dep != null) {
					//set the app incompat if one of its dependencies was incompat too
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
		for (EMCApp m : RegisteredApps.getRegisteredAppsList()) {
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
		for (EMCApp m : RegisteredApps.getRegisteredAppsList()) {
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
		for (EMCApp m : RegisteredApps.getRegisteredAppsList()) {
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
