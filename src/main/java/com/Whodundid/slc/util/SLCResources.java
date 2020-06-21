package com.Whodundid.slc.util;

import com.Whodundid.core.app.AppResources;
import com.Whodundid.core.util.resourceUtil.EResource;

public class SLCResources extends AppResources {

	public static final EResource SLSguiTexture = new EResource("skinlayerswitcher", "sls_options.png");
	public static final EResource SLSsettingsGuiSteve = new EResource("skinlayerswitcher", "sls_gui_steve.png");
	public static final EResource SLSsettingsGuiAlex = new EResource("skinlayerswitcher", "sls_gui_alex.png");
	public static final EResource SLSpartGui = new EResource("skinlayerswitcher", "sls_part_menu.png");
	
	public static final EResource logo0 = new EResource("skinlayerswitcher", "logo_0.png");
	public static final EResource logo1 = new EResource("skinlayerswitcher", "logo_1.png");
	public static final EResource icon = new EResource("skinlayerswitcher", "icon.png");
	
	@Override
	public void registerResources() {
		SLSguiTexture.register();
		SLSsettingsGuiSteve.register();
		SLSsettingsGuiAlex.register();
		SLSpartGui.register();
		
		logo0.register();
		logo1.register();
		icon.register();
	}
	
}
