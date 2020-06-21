package com.Whodundid.pingDrawer.util;

import com.Whodundid.core.app.AppResources;
import com.Whodundid.core.util.resourceUtil.EResource;

public class PingResources extends AppResources {

	public static final EResource logo = new EResource("pingdrawer", "logo.png");
	public static final EResource icon1 = new EResource("pingdrawer", "icon1.png");
	public static final EResource icon2 = new EResource("pingdrawer", "icon2.png");
	
	@Override
	public void registerResources() {
		logo.register();
		icon1.register();
		icon2.register();
	}

}
