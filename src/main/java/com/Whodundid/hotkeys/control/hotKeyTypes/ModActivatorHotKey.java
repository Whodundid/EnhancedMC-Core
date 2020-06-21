package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.util.AppEnabler;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import net.minecraft.util.EnumChatFormatting;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class ModActivatorHotKey extends HotKey {
	
	public AppType appType;
	
	public ModActivatorHotKey(String keyNameIn, KeyComboAction keysIn, AppType appIn) { this(keyNameIn, keysIn, appIn, false, "", null); }
	public ModActivatorHotKey(String keyNameIn, KeyComboAction keysIn, AppType appIn, boolean builtInVal) { this(keyNameIn, keysIn, appIn, builtInVal, "", null); }
	public ModActivatorHotKey(String keyNameIn, KeyComboAction keysIn, AppType appIn, String descriptionIn) { this(keyNameIn, keysIn, appIn, false, descriptionIn, null); }
	public ModActivatorHotKey(String keyNameIn, KeyComboAction keysIn, AppType appIn, boolean builtInVal, String descriptionIn, String builtInAppTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.APP_ACTIVATOR, builtInAppTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		appType = appIn;
	}
	
	public AppType getApp() { return appType; }
	public ModActivatorHotKey setApp(AppType typeIn) { appType = typeIn; return this; }
	
	@Override
	public void executeHotKeyAction() {
		AppEnabler.enableApp(appType, null);
		EChatUtil.show(EnumChatFormatting.GREEN + "Hokeys Enabled App: " + appType);
	}
	
	@Override
	public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + AppType.getAppName(appType));
		return base;
	}
	
}
