package com.Whodundid.guiCreator;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.guiCreator.gui.CreatorGui;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = GuiCreatorApp.MODID, version = GuiCreatorApp.VERSION, name = GuiCreatorApp.NAME, dependencies = "required-after:enhancedmc")
public final class GuiCreatorApp extends EMCApp {

	public static final String MODID = "guicreator";
	public static final String VERSION = "1.0";
	public static final String NAME = "EMC Gui Creator";
	
	private static GuiCreatorApp instance;
	
	public GuiCreatorApp() {
		super("EMC Gui Creator");
		instance = this;
		//shouldLoad = false;
		version = VERSION;
		author = "Whodundid";
		
		addDependency(AppType.CORE, "1.0");
		setMainGui(new CreatorGui());
		setAliases("creator", "guicreator", "egc");
	}
	
	public static GuiCreatorApp instance() { return instance; }
}
