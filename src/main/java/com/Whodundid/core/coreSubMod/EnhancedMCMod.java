package com.Whodundid.core.coreSubMod;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.EnhancedGui;
import com.Whodundid.core.settings.KeyBindGui;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.storageUtil.ModSetting;
import com.Whodundid.core.util.storageUtil.StorageBox;

public class EnhancedMCMod extends SubMod {
	
	public static final ModSetting emcMenuOverride = new ModSetting();
	
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
	
	@Override public SubMod getInstance() { return this; }
	
	@Override
	public EnhancedGui getMainGui(boolean setPosition, StorageBox<Integer, Integer> pos, EnhancedGui oldGui) {
		if (oldGui != null) { return setPosition ? new EMCSettingsGui(pos.getObject(), pos.getValue(), oldGui) : new EMCSettingsGui(oldGui); }
		return setPosition ? new EMCSettingsGui(pos.getObject(), pos.getValue()) : new EMCSettingsGui();
	}
}
