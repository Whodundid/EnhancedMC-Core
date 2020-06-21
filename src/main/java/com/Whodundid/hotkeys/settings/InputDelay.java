package com.Whodundid.hotkeys.settings;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.config.AppConfigSetting;
import net.minecraft.util.MathHelper;

public class InputDelay extends AppConfigSetting<Long> {
	
	public InputDelay() {
		super(Long.class, "keyInputDelay", "Key Input Delay", 1000l);
	}
	
	@Override
	public AppConfigSetting set(Long valIn) {
		long val = valIn;
		if (!EnhancedMC.isDevMode()) { val = (long) MathHelper.clamp_double(valIn, 500l, Long.MAX_VALUE); }
		return super.set(val);
	}

}
