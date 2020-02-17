package com.Whodundid.core.coreSubMod;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.subMod.config.CommentConfigBlock;
import com.Whodundid.core.subMod.config.ConfigBlock;
import com.Whodundid.core.subMod.config.CreateIfExistsConfigBlock;
import com.Whodundid.core.subMod.config.SubModConfigFile;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class EMCConfig extends SubModConfigFile {

	EMCMod mod;
	
	public EMCConfig(EMCMod modIn, String configNameIn) {
		super(modIn, configNameIn);
		mod = modIn;
	}
	
	@Override
	public boolean saveConfig() {
		
		EArrayList<ConfigBlock> configLines = new EArrayList();
		configLines.add(new CommentConfigBlock().addLine("EMC Core Config"));
		configLines.add(new ConfigBlock("Menu Override:", mod.menuOverride.get()).nl());
		configLines.add(new ConfigBlock("Draw Chat When Open:", mod.drawChatOnHud.get()).nl());
		configLines.add(new ConfigBlock("Show Incompats:", mod.showIncompats.get()).nl());
		configLines.add(new ConfigBlock("Enable Terminal:", mod.enableTerminal.get()).nl());
		configLines.add(new CreateIfExistsConfigBlock("enable debug:", Boolean.toString(EnhancedMC.isDebugMode())));
		
		return createConfig(configLines);
	}
	
	@Override
	public boolean loadConfig() {
		try {
			if (getConfigContents().size() > 0) {
				mod.menuOverride.set(getConfigVal("Menu Override:", Boolean.class, true));
				mod.drawChatOnHud.set(getConfigVal("Draw Chat When Open:", String.class, true));
				mod.showIncompats.set(getConfigVal("Show Incompats:", Boolean.class, true));
				mod.enableTerminal.set(getConfigVal("Enable Terminal:", Boolean.class, true));
				EnhancedMC.setDebugMode(getConfigVal("enable debug:", Boolean.class, false));
				return true;
			}
		} catch (Exception e) { e.printStackTrace(); }
		return false;
	}
}