package com.Whodundid.slc.config;

import com.Whodundid.core.app.config.CommentConfigBlock;
import com.Whodundid.core.app.config.ConfigBlock;
import com.Whodundid.core.app.config.AppConfigFile;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.slc.SLCApp;
import com.Whodundid.slc.util.PartModes;

public class SLCGlobalConfig extends AppConfigFile {
	
	static SLCApp mod;
	//static int profileNum, intervalValue;
	//static float versionNum;
	//static String facing = "", resetMode = "", running = "";
	
	public SLCGlobalConfig(SLCApp modIn, String configNameIn) {
		super(modIn, configNameIn);
		mod = modIn;
	}
	
	@Override
	public boolean saveConfig() {
		
		EArrayList<ConfigBlock> configLines = new EArrayList();
		
		configLines.add(new CommentConfigBlock("SLC Global Config **"));
		configLines.add(new CommentConfigBlock("NOTE: Only change these values if you know what you are doing!"));
		configLines.add(new CommentConfigBlock("Wrong values could potentially crash MC!"));
		configLines.add(new CommentConfigBlock("--------------------------"));
		configLines.add(new ConfigBlock("VERSION:", mod.getVersion()));
		configLines.add(new CommentConfigBlock("--------------------------"));
		configLines.add(new ConfigBlock("running:", mod.globalOn));
		configLines.add(new ConfigBlock("intervalValue:", mod.getCurrentInterval()));
		configLines.add(new ConfigBlock("defaultProfile:", mod.getDefaultLoadedProfile()));
		configLines.add(new ConfigBlock("defaultResetMode:", mod.resetMode.getMode()));
		configLines.add(new ConfigBlock("defaultFacing:", mod.getDefaultSkinFacing()));
		configLines.add(new CommentConfigBlock("--------------------------").nl());
		
		return createConfig(configLines);
	}

	@Override
	public boolean loadConfig() {
		try {
			if (getConfigContents().size() > 0) {
				float versionIn = getConfigVal("VERSION:", Float.class);
				boolean running = getConfigVal("running:", Boolean.class);
				int intervalValue = getConfigVal("intervalValue:", Integer.class);
				int profileNum = getConfigVal("defaultProfile:", Integer.class);
				String resetMode = getConfigVal("defaultResetMode:", String.class);
				String facing = getConfigVal("defaultFacing:", String.class);
				
				if (versionIn < Float.valueOf(mod.getVersion())) {
					System.out.println("WARNING! Using older configs in skin switcher could cause unpredicatble events!");
				}
				if (versionIn > Float.valueOf(mod.getVersion())) {
					System.out.println("WARNING! Future config version detected! Unpredicatble results may occur!");
				}
				
				mod.globalOn = running;
				
				if (intervalValue < 0 || intervalValue > 100000) { intervalValue = 25; }
				if (profileNum < 1 || profileNum > 4) { profileNum = 1; }
				if (!facing.equalsIgnoreCase("front") && !facing.equalsIgnoreCase("back")) { facing = "front"; }
				
				switch (resetMode) {
				case "switch": mod.resetMode = PartModes.SW; break;
				case "blink": mod.resetMode = PartModes.BL; break;
				case "none": mod.resetMode = PartModes.N; break;
				}
				
				mod.currentChangeValue = intervalValue;
				mod.currentLoadedProfile = profileNum;
				mod.defaultLoadedProfile = profileNum;
				mod.skinFrontFacing = !facing.equalsIgnoreCase("back");
				
				return true;
			}
		} catch (Exception e) { e.printStackTrace(); }
		return false;
	}
	
	@Override
	public boolean resetConfig() {
		
		EArrayList<ConfigBlock> configLines = new EArrayList();
		
		configLines.add(new CommentConfigBlock("SLC Global Config **"));
		configLines.add(new CommentConfigBlock("NOTE: Only change these values if you know what you are doing!"));
		configLines.add(new CommentConfigBlock("Wrong values could potentially crash MC!"));
		configLines.add(new CommentConfigBlock("--------------------------"));
		configLines.add(new ConfigBlock("VERSION:", mod.getVersion()));
		configLines.add(new CommentConfigBlock("--------------------------"));
		configLines.add(new ConfigBlock("running:", true));
		configLines.add(new ConfigBlock("intervalValue:", 25));
		configLines.add(new ConfigBlock("defaultProfile:", 1));
		configLines.add(new ConfigBlock("defaultResetMode:", PartModes.N));
		configLines.add(new ConfigBlock("defaultFacing:", "front"));
		configLines.add(new CommentConfigBlock("--------------------------").nl());
		
		return createConfig(configLines);
	}
	
	/*
	private static void updateValues(boolean onlyRunning) {
		if (versionNum < Float.valueOf(mod.getVersion())) {
			System.out.println("WARNING! Using older configs in skin switcher could cause unpredicatble events!");
		}
		if (versionNum > Float.valueOf(mod.getVersion())) {
			System.out.println("WARNING! Future config version detected! Unpredicatble results may occur!");
		}
		
		mod.globalOn = running.equalsIgnoreCase("true");
		
		if (!onlyRunning) {
			
		}		
	}
	
	@Deprecated
	public static void updateGlobalConfig() {
		try {
			File config = new File("EnhancedMC/SkinLayerSwitcher/");
			if (!config.exists()) { config.mkdirs(); }
			PrintWriter writer = new PrintWriter("EnhancedMC/SkinLayerSwitcher/SLS_Mod_Config.cfg", "UTF-8");
			writer.println("** SLS Global Config **");
			writer.println("** NOTE: Only change these values if you know what you are doing!");
			writer.println("** Wrong values could potentially crash MC!");
			writer.println("** --------------------------");
			writer.println("VERSION: " + mod.getVersion());
			writer.println("** --------------------------");
			writer.println("running: " + mod.globalOn); //move to skin profiles
			writer.println("intervalValue: " + mod.getCurrentInterval());
			writer.println("defaultProfile: " + mod.getDefaultLoadedProfile());
			writer.println("defaultResetMode: " + mod.resetMode.getMode());
			writer.println("defaultFacing: " + mod.getDefaultSkinFacing());
			writer.println("** --------------------------");
			writer.print("END");
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) { e.printStackTrace(); }
	}
	
	@Deprecated
	public static void loadGlobalConfig(boolean onlyRunning) {
		File globalConfig = new File("EnhancedMC/SkinLayerSwitcher/SLS_Mod_Config.cfg");
		if (globalConfig.exists()) {
			boolean isEnd = false;
			resetValues();
			String command, configLine;
			try (Scanner fileReader = new Scanner(globalConfig)) {
				while (!isEnd) {
					configLine = fileReader.nextLine();
					Scanner line = new Scanner(configLine);
					command = line.next();
					switch (command) {
					case "**": break;
					case "VERSION:": versionNum = Float.valueOf(line.next()); break;
					case "running:": running = line.next(); break;
					case "intervalValue:": intervalValue = Integer.valueOf(line.next()); break;
					case "defaultProfile:": profileNum = Integer.valueOf(line.next()); break;
					case "defaultResetMode:": resetMode = line.next(); break;
					case "defaultFacing:": facing = line.next(); break;					
					case "END": isEnd = true;
					default: break;
					}
					line.close();
				}
				fileReader.close();
				//System.out.println("SLS Global Config Loaded");
				updateValues(onlyRunning);
				return;
			} catch (FileNotFoundException e) {}
		}
		updateGlobalConfig();
		loadGlobalConfig(false);
	}
	
	private static void resetValues() {
		profileNum = 1; intervalValue = 25;
		versionNum = Float.valueOf(mod.getVersion());
		facing = "Front";
		resetMode = "none";
	}
	
	*/
}