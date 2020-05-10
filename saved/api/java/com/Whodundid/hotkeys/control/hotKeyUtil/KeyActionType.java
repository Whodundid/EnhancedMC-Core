package com.Whodundid.hotkeys.control.hotKeyUtil;

//Sep 30, 2018
//Last edited: Feb 18, 2019
//Edit note: finally added key category activator/deactivator keys.
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public enum KeyActionType {
	MESSAGESENDER(true, "Chat Message Sender"),
	CONDITIONAL_MESSAGE_ITEMTEST(true, "Conditional Chat Message Sender"),
	CUSTOM(false, "Custom"),
	MOD_ACTIVATOR(true, "EMC Mod Activator"),
	MOD_DEACTIVATOR(true, "EMC Mod Deactivator"),
	CATEGORY_ACTIVATOR(true, "Key Category Activator"),
	CATEGORY_DEACTIVATOR(true, "Key Category Deactivator"),
	GUI_OPENER(true, "Gui Opener"),
	MC_KEYBIND_MODIFIER(false, "KeyBind Modifier"),
	DEBUG(true, "Debug Command Runner"),
	SUBMOD(false, "SubMod Hotkey"),
	UNDEFINED(false, "none");
	
	private boolean canUserCreate;
	private String keyStringName;
	
	private KeyActionType(boolean canUserCreateIn, String stringNameIn) {
		canUserCreate = canUserCreateIn;
		keyStringName = stringNameIn;
	}
	
	public boolean canUserCreate() { return canUserCreate; }
	
	public static String getStringFromType(KeyActionType typeIn) {
		return typeIn.keyStringName;
	}
	
	public static KeyActionType getActionTypeFromString(String typeIn) {
		try {
			return valueOf(typeIn);
		} catch (IllegalArgumentException e) {}
		
		for (KeyActionType type : values()) {
			if (type.keyStringName.equals(typeIn)) { return type; }
		}
		
		return UNDEFINED;
	}
}
