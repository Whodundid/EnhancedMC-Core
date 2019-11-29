package com.Whodundid.core.coreSubMod;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.settings.KeyBindGui;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.storageUtil.ModSetting;
import net.minecraftforge.client.event.MouseEvent;
import org.lwjgl.input.Keyboard;

public class EnhancedMCMod extends SubMod {
	
	public static final ModSetting emcMenuOverride = new ModSetting();
	public static final ModSetting useDebugKey = new ModSetting(false);
	public static final ModSetting showIncompats = new ModSetting(false);
	public static final ModSetting enableConsole = new ModSetting(false);
	
	public EnhancedMCMod() {
		super(SubModType.CORE);
		version = EnhancedMC.VERSION;
		author = "Whodundid";
		configManager.setMainConfig(new CoreConfig(this, "enhancedMCCore"));
		setEnabled(true);
		setMainGui(new EMCSettingsGui());
		addGui(new SettingsGuiMain(), new KeyBindGui());
		isDisableable = false;
	}
}
