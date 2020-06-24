package com.Whodundid.core.app;

import com.Whodundid.core.EnhancedMC;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import org.apache.logging.log4j.Level;

//Author: Hunter Bragg

public class AppSettings {
	
	public static void updateAppState(EMCApp appIn, boolean state) { updateAppState(appIn, state, true); }
	public static void updateAppState(EMCApp appIn, boolean state, boolean reloadWindows) {
		for (EMCApp m : RegisteredApps.getRegisteredAppsList()) {
			if (m == appIn) {
				boolean previous = m.isEnabled();
				
				if (!m.isIncompatible()) {
					if (m.isDisableable()) {
						m.setEnabled(state);
						saveConfig();
					}
					else {
						if (!m.isEnabled() && state == false) {
							m.setEnabled(state);
						}
					}
					
					if (previous && !state) { m.onDisabled(); }
					
					if (reloadWindows) { EnhancedMC.reloadAllWindows(); }
				}
				break;
			}
		}
	}
	
	public static void updateAppState(AppType typeIn, boolean state) { updateAppState(typeIn.appName, state, true); }
	public static void updateAppState(AppType typeIn, boolean state, boolean reloadWindows) { updateAppState(typeIn.appName, state, reloadWindows); }
	public static void updateAppState(String appName, boolean state) { updateAppState(appName, state, true); }
	public static void updateAppState(String appName, boolean state, boolean reloadWindows) {
		EMCApp m = RegisteredApps.getApp(appName);
		if (m != null) {
			boolean previous = m.isEnabled();
			
			if (!m.isIncompatible()) {
				if (m.isDisableable()) {
					m.setEnabled(state);
					saveConfig();
				}
				else {
					if (!m.isEnabled() && state == false) {
						m.setEnabled(state);
					}
				}
				
				if (previous && !state) { m.onDisabled(); }
				
				if (reloadWindows) { EnhancedMC.reloadAllWindows(); }
			}
		}
	}
	
	/** Sets all mods to disabled and rebuilds the enabled apps file. */
	public static void resetConfig() { saveConfig(true); }
	
	public static void saveConfig() { saveConfig(false); }
	
	private static void saveConfig(boolean reset) {
		if (reset) { RegisteredApps.getRegisteredAppsList().forEach(m -> updateAppState(m, false)); }
		
		try {
			File settings = new File("EnhancedMC/");
			if (!settings.exists()) { settings.mkdirs(); }
			PrintWriter saver = new PrintWriter("EnhancedMC/EMCApps.cfg" , "UTF-8");
			
			saver.println("EMC Apps");
			saver.println();
			for (EMCApp m : RegisteredApps.getRegisteredAppsList()) {
				String saveName = m.getAppType() != null ? m.getAppType().toString() : m.getName();
				saver.println(saveName  + " " + m.isEnabled());
			}
			saver.println();
			saver.print("END");
			saver.close();
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public static void loadConfig() {
		try {
			File loadedSettings = new File("EnhancedMC/EMCApps.cfg");
			if (loadedSettings.exists()) {
				Scanner loader = new Scanner(loadedSettings);
				while (loader.hasNextLine()) {
					if (loader.nextLine().equals("EMC Apps")) {
						while (loader.hasNextLine()) {
							String line = loader.nextLine();
							Scanner lineReader = new Scanner(line);
							if (!line.equals("END")) {
								if (lineReader.hasNext()) {
									String readMod = lineReader.next();
									//boolean isType = false;
									
									AppType type = AppType.getTypeFromString(readMod);
									//isType = type != null;
									
									if (lineReader.hasNext()) {
										String valIn = lineReader.next();
										//System.out.println("the app: " + readMod + " ;; " + type);
										boolean val = false;
										try {
											val = Boolean.parseBoolean(valIn);
										}
										catch (Exception f) { EnhancedMC.log(Level.WARN, "failed to parse emc EMCApps file line: " + valIn); val = false; }
										updateAppState(type != null ? type.appName : readMod, val);
									}
								}
								lineReader.close();
							}
							else { break; }
						}
					}
				}
				loader.close();
			}
			else { saveConfig(); }
		}
		catch (Exception e) { e.printStackTrace(); saveConfig(); }
	}
	
}
