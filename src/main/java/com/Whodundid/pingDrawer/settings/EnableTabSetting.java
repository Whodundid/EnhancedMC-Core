package com.Whodundid.pingDrawer.settings;

import com.Whodundid.core.app.config.AppConfigSetting;
import com.Whodundid.pingDrawer.PingApp;

public class EnableTabSetting extends AppConfigSetting<Boolean> {
	
	public EnableTabSetting() {
		super(Boolean.class, "enableTab", "Enable Enhanced Tab", true);
	}
	
	@Override
	public AppConfigSetting set(Boolean valIn) {
		super.set(valIn);
		
		if (!valIn) { PingApp.removeEnhancedTab(); }
		else { PingApp.addEnhancedTab(); }
		
		return this;
	}

}
