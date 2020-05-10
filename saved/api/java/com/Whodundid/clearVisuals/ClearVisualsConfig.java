package com.Whodundid.clearVisuals;

import com.Whodundid.core.app.config.CommentConfigBlock;
import com.Whodundid.core.app.config.ConfigBlock;
import com.Whodundid.core.app.config.AppConfigFile;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Dec 29, 2018 : Initial
//Last edited: Jul 10, 2019
//Edit note: Added 'Remove Fog', Renamed drawFire -> removeFire, Renamed drawWaterOverlay -> removeWaterOverlay
//First Added: Dec 29, 2018
//Author: Hunter Bragg

public class ClearVisualsConfig extends AppConfigFile {

	ClearVisualsApp mod;
	
	public ClearVisualsConfig(ClearVisualsApp modIn, String configNameIn) {
		super(modIn, configNameIn);
		mod = modIn;
	}
	
	@Override
	public boolean saveConfig() {
		
		EArrayList<ConfigBlock> configLines = new EArrayList();
		configLines.add(new CommentConfigBlock("Clear Visuals Config").nl());
		configLines.add(new ConfigBlock("Gama Value:", mod.getModGama()));
		configLines.add(new ConfigBlock("Remove Fire:", mod.isFireDrawn()));
		configLines.add(new ConfigBlock("Remove Water Overlay:", mod.isWaterOverlayDrawn()));
		configLines.add(new ConfigBlock("Remove Sky Overlay:", mod.isFogDrawn()));
		configLines.add(new ConfigBlock("Clear Lava:", mod.isClearLava()));
		configLines.add(new ConfigBlock("Clear Water:", mod.isClearWater()).nl());
		
		return createConfig(configLines);
	}
	
	@Override
	public boolean loadConfig() {
		try {
			if (getConfigContents().size() > 0) {
				mod.setGama(getConfigVal("Gama Value:", Float.class));
				mod.setFireVisibility(getConfigVal("Remove Fire:", Boolean.class));
				mod.setUnderWaterOverlayVisibility(getConfigVal("Remove Water Overlay:", Boolean.class));
				mod.setFogVisibility(!getConfigVal("Remove Sky Overlay:", Boolean.class));
				mod.setClearLava(getConfigVal("Clear Lava:", Boolean.class));
				mod.setClearWater(getConfigVal("Clear Water:", Boolean.class));
				return true;
			}
		} catch (Exception e) { e.printStackTrace(); }
		return false;
	}
	
	@Override
	public boolean resetConfig() {
		
		EArrayList<ConfigBlock> configLines = new EArrayList();
		configLines.add(new CommentConfigBlock("Clear Visuals Config").nl());
		configLines.add(new ConfigBlock("Gama Value:", 1.0f));
		configLines.add(new ConfigBlock("Remove Fire:", true));
		configLines.add(new ConfigBlock("Remove Water Overlay:", true));
		configLines.add(new ConfigBlock("Remove Sky Overlay:", true));
		configLines.add(new ConfigBlock("Clear Lava:", true));
		configLines.add(new ConfigBlock("Clear Water:", true).nl());
		
		return createConfig(configLines);
	}
}
