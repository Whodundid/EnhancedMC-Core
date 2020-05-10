package com.Whodundid.hotkeys;

import com.Whodundid.core.app.config.CommentConfigBlock;
import com.Whodundid.core.app.config.ConfigBlock;
import com.Whodundid.core.app.config.AppConfigFile;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class HotKeyConfig extends AppConfigFile {

	HotKeyApp mod;
	
	public HotKeyConfig(HotKeyApp modIn, String configNameIn) {
		super(modIn, configNameIn);
		mod = modIn;
	}
	
	@Override
	public boolean saveConfig() {
		
		EArrayList<ConfigBlock> configLines = new EArrayList();
		
		configLines.add(new CommentConfigBlock("HotKeys Config").nl());
		configLines.add(new ConfigBlock("Ctrl Key Stops Movement:", mod.doesCtrlKeyStopMovement()));
		configLines.add(new ConfigBlock("Run Creation Tutorial:", mod.runCreationTutorial));
		configLines.add(new ConfigBlock("Create Example HotKey:", mod.createExampleKey).nl());
		
		return createConfig(configLines);
	}
	
	@Override
	public boolean loadConfig() {
		try {
			if (getConfigContents().size() > 0) {
				mod.setStopMovementOnPress(getConfigVal("Ctrl Key Stops Movement:", Boolean.class));
				mod.setRunTutorial(getConfigVal("Run Creation Tutorial:", Boolean.class));
				mod.setCreateExampleKey(getConfigVal("Create Example HotKey:", Boolean.class));
				//mod.loadHotKeys();
				return true;
			}
		} catch (Exception e) { e.printStackTrace(); }
		return false;
	}
	
	@Override
	public boolean resetConfig() {
		
		EArrayList<ConfigBlock> configLines = new EArrayList();
		
		configLines.add(new CommentConfigBlock("HotKeys Config").nl());
		configLines.add(new ConfigBlock("Ctrl Key Stops Movement:", false));
		configLines.add(new ConfigBlock("Run Creation Tutorial:", true));
		configLines.add(new ConfigBlock("Create Example HotKey:", true).nl());
		
		return createConfig(configLines);
	}
}
