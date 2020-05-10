package com.Whodundid.slc;

import net.minecraft.util.ResourceLocation;

public class SLCResources {

	public static final ResourceLocation SLSguiTexture;
	public static final ResourceLocation SLSsettingsGuiSteve;
	public static final ResourceLocation SLSsettingsGuiAlex;
	public static final ResourceLocation SLSpartGui;
	
	static {
		SLSguiTexture = new ResourceLocation("skinlayerswitcher", "sls_options.png");
		SLSsettingsGuiSteve = new ResourceLocation("skinlayerswitcher", "sls_gui_steve.png");
		SLSsettingsGuiAlex = new ResourceLocation("skinlayerswitcher", "sls_gui_alex.png");
		SLSpartGui = new ResourceLocation("skinlayerswitcher", "sls_part_menu.png");
	}
}
