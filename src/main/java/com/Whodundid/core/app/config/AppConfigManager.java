package com.Whodundid.core.app.config;

import java.io.File;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;

//Author: Hunter Bragg

public class AppConfigManager {

	protected EMCApp mod;
	protected AppConfigFile mainConfig = null;
	protected StorageBoxHolder<String, AppConfigFile> configFiles = new StorageBoxHolder();
	
	public AppConfigManager(EMCApp modIn) {
		mod = modIn;
		
		//check if directory exists
		File basePath = RegisteredApps.getAppConfigBaseFileLocation(mod.getName());
		if (!basePath.exists()) { basePath.mkdirs(); }
	}
	
	public void setMainConfig(AppConfigFile configIn) {
		mainConfig = configIn;
		addConfig(configIn);
	}
	
	public void addConfig(AppConfigFile... configsIn) {
		for (AppConfigFile f : configsIn) { configFiles.add(f.getConfigName(), f); }
	}
	
	public boolean saveAllConfigs() { return !configFiles.getValues().stream().anyMatch(f -> !f.saveConfig()); }
	public boolean loadAllConfigs() { return !configFiles.getValues().stream().anyMatch(f -> !f.loadConfig()); }
	public boolean resetAllConfigs() { return !configFiles.getValues().stream().anyMatch(f -> !f.resetConfig()); }
	
	public boolean saveMainConfig() { return mainConfig != null ? mainConfig.saveConfig() : false; }
	public boolean loadMainConfig() { return mainConfig != null ? mainConfig.loadConfig() : false; }
	public boolean resetMainConfig() { return mainConfig != null ? mainConfig.resetConfig() : false; }
	
	public boolean saveConfig(String configName) {
		StorageBox<String, AppConfigFile> box = configFiles.getBoxWithObj(configName);
		return box != null ? box.getValue().saveConfig() : false;
	}
	
	public boolean loadConfig(String configName) {
		StorageBox<String, AppConfigFile> box = configFiles.getBoxWithObj(configName);
		return box != null ? box.getValue().loadConfig() : false;
	}
	
	public boolean resetConfig(String configName) {
		StorageBox<String, AppConfigFile> box = configFiles.getBoxWithObj(configName);
		return box != null ? box.getValue().resetConfig() : false;
	}
	
	public AppConfigFile getMainConfig() { return mainConfig; }
	
	public File getConfigFileLocation(String configName) { return new File(RegisteredApps.getAppConfigBaseFileLocation(mod.getAppType()).getAbsolutePath() + "/" + configName + ".cfg"); }
	public int getNumberOfConfigFiles() { return configFiles.size(); }
}
