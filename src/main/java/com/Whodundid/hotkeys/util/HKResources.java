package com.Whodundid.hotkeys.util;

import com.Whodundid.core.app.AppResources;
import com.Whodundid.core.util.resourceUtil.EResource;

public class HKResources extends AppResources {

	public static final EResource logo = new EResource("hotkeys", "logo.png");
	public static final EResource icon = new EResource("hotkeys", "icon.png");
	public static final EResource iconList = new EResource("hotkeys", "icon_list.png");
	public static final EResource iconCreator = new EResource("hotkeys", "icon_creator.png");

	@Override
	public void registerResources() {
		logo.register();
		icon.register();
		iconList.register();
		iconCreator.register();
	}
	
}
