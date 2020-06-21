package com.Whodundid.clearVisuals.util;

import com.Whodundid.core.app.AppResources;
import com.Whodundid.core.util.resourceUtil.EResource;

public class CVResources extends AppResources {

	public static final EResource logo = new EResource("clearvisuals", "logo.png");
	public static final EResource icon = new EResource("clearvisuals", "icon.png");

	@Override
	public void registerResources() {
		logo.register();
		icon.register();
	}
	
}
