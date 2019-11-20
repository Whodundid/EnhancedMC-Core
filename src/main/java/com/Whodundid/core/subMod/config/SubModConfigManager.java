package com.Whodundid.core.subMod.config;

import java.io.File;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;

//Last edited: Dec 12, 2018
//First Added: Dec 12, 2018
//Author: Hunter Bragg

public class SubModConfigManager {

	protected SubMod mod;
	protected SubModConfigFile mainConfig = null;
	protected StorageBoxHolder<String, SubModConfigFile> configFiles = new StorageBoxHolder();
	
	public SubModConfigManager(SubMod modIn) {
		mod = modIn;
		
		//check if directory exists
		File basePath = RegisteredSubMods.getModConfigBaseFileLocation(mod.getName());
		if (!basePath.exists()) { basePath.mkdirs(); }
	}
	
	public void setMainConfig(SubModConfigFile configIn) {
		mainConfig = configIn;
		addConfig(configIn);
	}
	
	public void addConfig(SubModConfigFile... configsIn) {
		for (SubModConfigFile f : configsIn) { configFiles.add(f.getConfigName(), f); }
	}
	
	public boolean saveAllConfigs() {
		for (SubModConfigFile f : configFiles.getValues()) { if (!f.saveConfig()) { return false; } }
		return true;
	}
	
	public boolean loadAllConfigs() {
		for (SubModConfigFile f : configFiles.getValues()) { if (!f.loadConfig()) { return false; } }
		return true;
	}
	
	public boolean saveMainConfig() {
		if (mainConfig != null) { return mainConfig.saveConfig(); }
		return false;
	}
	
	public boolean loadMainConfig() {
		if (mainConfig != null) { return mainConfig.loadConfig(); }
		return false;
	}
	
	public boolean saveConfig(String configName) {
		StorageBox<String, SubModConfigFile> box = configFiles.getBoxWithObj(configName);
		return box != null ? box.getValue().saveConfig() : false;
	}
	public boolean loadConfig(String configName) {
		StorageBox<String, SubModConfigFile> box = configFiles.getBoxWithObj(configName);
		return box != null ? box.getValue().loadConfig() : false;
	}
	
	public SubModConfigFile getMainConfig() { return mainConfig; }
	
	public File getConfigFileLocation(String configName) { return new File(RegisteredSubMods.getModConfigBaseFileLocation(mod.getModType()).getAbsolutePath() + "/" + configName + ".cfg"); }
	public int getNumberOfConfigFiles() { return configFiles.size(); }
}
