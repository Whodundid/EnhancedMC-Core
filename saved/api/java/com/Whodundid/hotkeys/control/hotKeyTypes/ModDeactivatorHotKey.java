package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.core.app.AppSettings;
import com.Whodundid.core.app.AppType;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class ModDeactivatorHotKey extends HotKey {
	
	public AppType modType;
	
	public ModDeactivatorHotKey(String keyNameIn, KeyComboAction keysIn, AppType modIn) { this(keyNameIn, keysIn, modIn, false, "", null); }
	public ModDeactivatorHotKey(String keyNameIn, KeyComboAction keysIn, AppType modIn, boolean builtInVal) { this(keyNameIn, keysIn, modIn, builtInVal, "", null); }
	public ModDeactivatorHotKey(String keyNameIn, KeyComboAction keysIn, AppType modIn, String descriptionIn) { this(keyNameIn, keysIn, modIn, false, descriptionIn, null); }
	public ModDeactivatorHotKey(String keyNameIn, KeyComboAction keysIn, AppType modIn, boolean builtInVal, String descriptionIn, String builtInSubModTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.MOD_DEACTIVATOR, builtInSubModTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		modType = modIn;
	}
	
	public AppType getSubMod() { return modType; }
	
	@Override
	public void executeHotKeyAction() {
		//if (modType.equals(SubModType.ALL)) {
		//	for (SubMod m : RegisteredSubMods.getRegisteredModsList()) {
		//		GlobalSettings.updateSetting(m, false);
		//	}
		//} else {
			AppSettings.updateAppState(modType, false);
		//}
	}
	public ModDeactivatorHotKey setSubMod(AppType typeIn) { modType = typeIn; return this; }
	
	@Override
	public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + AppType.getAppName(modType));
		return base;
	}
}
