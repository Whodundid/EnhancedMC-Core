package com.Whodundid.core.coreSubMod;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.types.EnhancedGui;
import com.Whodundid.core.renderer.IRendererProxy;
import com.Whodundid.core.settings.KeyBindGui;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.storageUtil.ModSetting;
import net.minecraftforge.client.event.GuiScreenEvent;

public class EnhancedMCMod extends SubMod {
	
	public static final ModSetting emcMenuOverride = new ModSetting(true);
	public static final ModSetting useDebugKey = new ModSetting(false);
	public static final ModSetting showIncompats = new ModSetting(false);
	public static final ModSetting enableTerminal = new ModSetting(false);
	public static final ModSetting drawChatOnGui = new ModSetting(true);
	
	public EnhancedMCMod() {
		super(SubModType.CORE);
		version = EnhancedMC.VERSION;
		author = "Whodundid";
		configManager.setMainConfig(new CoreConfig(this, "enhancedMCCore"));
		setEnabled(true);
		setMainGui(new CoreSettingsGui());
		addGui(new SettingsGuiMain(), new KeyBindGui());
		isDisableable = false;
	}
	
	@Override
	public void eventInitGui(GuiScreenEvent.InitGuiEvent e) {
		if (!(e.gui instanceof EnhancedGui || e.gui instanceof IRendererProxy)) {
			EnhancedMC.getRenderer().removeUnpinnedObjects();
		}
	}
}
