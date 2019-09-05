package com.Whodundid.core.coreSubMod;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.subMod.config.CommentConfigBlock;
import com.Whodundid.core.subMod.config.ConfigBlock;
import com.Whodundid.core.subMod.config.CreateIfExistsConfigBlock;
import com.Whodundid.core.subMod.config.SubModConfigFile;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class CoreConfig extends SubModConfigFile {

	EnhancedMCMod mod;
	
	public CoreConfig(EnhancedMCMod modIn, String configNameIn) {
		super(modIn, configNameIn);
		mod = modIn;
	}
	
	@Override
	public boolean saveConfig() {
		
		EArrayList<ConfigBlock> configLines = new EArrayList();
		configLines.add(new CommentConfigBlock().addLine("EMC Core Config"));
		configLines.add(new ConfigBlock("Menu Override:", mod.emcMenuOverride.get()).createEmptyLine(!doesFileContainIdentifier("enable debug:")));
		configLines.add(new CreateIfExistsConfigBlock("enable debug:", Boolean.toString(EnhancedMC.isDebugMode())));
		
		return createConfig(configLines);
	}
	
	@Override
	public boolean loadConfig() {
		try {
			if (getConfigContents().size() > 0) {
				mod.emcMenuOverride.set(getConfigVal("Menu Override:", Boolean.class));
				EnhancedMC.setDebugMode(getConfigVal("enable debug:", Boolean.class, false));
				
				return true;
			}
		} catch (Exception e) { e.printStackTrace(); }
		return false;
	}
}
