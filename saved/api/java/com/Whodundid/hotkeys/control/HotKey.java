package com.Whodundid.hotkeys.control;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.hotkeys.HotKeyApp;
import com.Whodundid.hotkeys.control.hotKeyUtil.HotKeyActionException;
import com.Whodundid.hotkeys.control.hotKeyUtil.HotkeyCategory;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import net.minecraft.client.Minecraft;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public abstract class HotKey {
	
	protected Minecraft mc = Minecraft.getMinecraft();
	protected HotKeyApp man = (HotKeyApp) RegisteredApps.getApp(AppType.HOTKEYS);
	protected String keyName = "";
	protected String description = "No description set.";
	protected KeyComboAction keyCombo;
	protected HotkeyCategory category;
	protected boolean isEnabled = true;
	protected boolean builtIn = false;
	protected String builtInSubModType = "";
	protected EArrayList<KeyActionType> actions;
	protected KeyActionType hotKeyType;
	
	protected HotKey() {}
	public HotKey(String keyNameIn, KeyComboAction keyCodeIn, KeyActionType typeIn) { this(keyNameIn, keyCodeIn, true, typeIn, null); }
	public HotKey(String keyNameIn, KeyComboAction keyCodeIn, KeyActionType typeIn, String builtInSubModTypeIn) { this(keyNameIn, keyCodeIn, true, typeIn, builtInSubModTypeIn); }
	public HotKey(String keyNameIn, KeyComboAction keyCodeIn, boolean builtInVal, KeyActionType typeIn) { this(keyNameIn, keyCodeIn, builtInVal, typeIn, null); }
	public HotKey(String keyNameIn, KeyComboAction keyCodeIn, boolean builtInVal, KeyActionType typeIn, String builtInSubModTypeIn) { this(keyNameIn, keyCodeIn, builtInVal, typeIn, null, builtInSubModTypeIn); }
	public HotKey(String keyNameIn, KeyComboAction keyCodeIn, boolean builtInVal, KeyActionType typeIn, String categoryNameIn, String builtInSubModTypeIn) {
		keyName = keyNameIn;
		keyCombo = keyCodeIn;
		builtIn = builtInVal;
		hotKeyType = typeIn;
		builtInSubModType = builtInSubModTypeIn;
		if (categoryNameIn != null) { category = new HotkeyCategory(categoryNameIn); }
	}
	
	//getters
	public String getKeyName() { return keyName; }
	public String getKeyDescription() { return description; }
	public KeyComboAction getKeyCombo() { return keyCombo; }
	public KeyActionType getHotKeyType() { return hotKeyType; }
	public HotkeyCategory getKeyCategory() { return category; }
	public String getBuiltInSubModType() { return isSubModKey() ? builtInSubModType : "not submod key"; }
	
	//setters
	public void setEnabled(boolean enable) { isEnabled = enable; }
	public void setKeyCategory(String categoryNameIn) { category = new HotkeyCategory(categoryNameIn); }
	public void setKeyDescription(String descriptionIn) { description = descriptionIn; }
	
	//general
	public boolean isEnabled() { return isEnabled; }
	public boolean isSubModKey() { return builtIn; }
	
	public HotKey addAction(KeyActionType typeIn) throws HotKeyActionException {
		if (actions.contains(typeIn)) { throw new HotKeyActionException("Cannot have multiple actions of the same type in one key!"); }
		actions.add(typeIn);
		return this;
	}
	
	public HotKey removeAction(KeyActionType typeIn) throws HotKeyActionException {
		if (!actions.contains(typeIn)) { throw new HotKeyActionException("HotKey: " + keyName + " does not contain the action: " + typeIn.getStringFromType(typeIn)); }
		actions.remove(typeIn);
		return this;
	}
	
	public HotKey changeAction(KeyActionType oldAction, KeyActionType newAction) throws HotKeyActionException {
		if (!actions.contains(oldAction)) { throw new HotKeyActionException("Cannot replace an action that doesn't exist!"); }
		if (actions.contains(newAction)) { throw new HotKeyActionException("Cannot have multiple actions of the same type in one key!"); }
		actions.remove(oldAction);
		actions.add(newAction);
		return this;
	}
	
	public String getHotKeyStatistics() {
		String returnStats = keyName + "; ";
		returnStats += hotKeyType.toString() + "; ";
		for (int i : keyCombo.hotKeyCodes) { returnStats += (i + " "); }
		returnStats += "; ";
		returnStats += String.valueOf(isEnabled) + "; ";
		if (category != null) { returnStats += category.getCategoryName(); }
		returnStats += String.valueOf(builtIn);
		return returnStats;
	}
	
	public abstract void executeHotKeyAction();
}
