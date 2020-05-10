package com.Whodundid.pingDrawer;

import com.Whodundid.core.app.config.CommentConfigBlock;
import com.Whodundid.core.app.config.ConfigBlock;
import com.Whodundid.core.app.config.AppConfigFile;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Last edited: Dec 12, 2018
//First Added: Dec 12, 2018
//Author: Hunter Bragg

public class PingConfig extends AppConfigFile {

	PingApp mod;
	
	public PingConfig(PingApp modIn, String configNameIn) {
		super(modIn, configNameIn);
		mod = modIn;
	}
	
	@Override
	public boolean saveConfig() {
		
		EArrayList<ConfigBlock> configLines = new EArrayList();
		
		configLines.add(new CommentConfigBlock("Ping Config", "Screen location, custom position (x, y) values, drawWithChatOpen").nl());
		configLines.add(new ConfigBlock("ScreenLocation:", mod.loc.asString()));
		configLines.add(new ConfigBlock("Position:", mod.getLocation().getObject(), mod.getLocation().getValue()));
		configLines.add(new ConfigBlock("Draw with Chat:", mod.drawWithChatOpen).nl());
		
		return createConfig(configLines);
	}

	@Override
	public boolean loadConfig() {
		try {
			if (getConfigContents().size() > 0) {
				ScreenLocation loc = ScreenLocation.valueOf(getConfigVal("ScreenLocation:", String.class));
				if (loc.equals(ScreenLocation.custom)) {
					EArrayList<String> values = configValues.getBoxWithObj("Position:").getValue();
					if (values != null && values.size() == 2) {
						int x, y;
						x = Integer.parseInt(values.get(0));
						y = Integer.parseInt(values.get(1));
						mod.setLocation(x, y);
					} else { return false; }
				} else { mod.setLocation(loc); }
				mod.setDrawWithChatOpen(getConfigVal("Draw with Chat:", Boolean.class));
				return true;
			}
		} catch (Exception e) { e.printStackTrace(); }
		return false;
	}
	
	@Override
	public boolean resetConfig() {
		
		EArrayList<ConfigBlock> configLines = new EArrayList();
		
		configLines.add(new CommentConfigBlock("Ping Config", "Screen location, custom position (x, y) values, drawWithChatOpen").nl());
		configLines.add(new ConfigBlock("ScreenLocation:", mod.loc.defAsString()));
		configLines.add(new ConfigBlock("Position:", mod.getLocation().getObject(), mod.getLocation().getValue()));
		configLines.add(new ConfigBlock("Draw with Chat:", mod.drawWithChatOpen).nl());
		
		return createConfig(configLines);
	}
}
