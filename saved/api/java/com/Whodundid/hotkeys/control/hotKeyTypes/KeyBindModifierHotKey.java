package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import net.minecraft.client.settings.KeyBinding;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class KeyBindModifierHotKey extends HotKey {
	
	public KeyBinding keyBind;
	public boolean val;
	
	public KeyBindModifierHotKey(String keyNameIn, KeyComboAction keysIn, KeyBinding mcKeyIn, boolean newVal) { this(keyNameIn, keysIn, mcKeyIn, newVal, false, "", null); }
	public KeyBindModifierHotKey(String keyNameIn, KeyComboAction keysIn, KeyBinding mcKeyIn, boolean newVal, boolean builtInVal) { this(keyNameIn, keysIn, mcKeyIn, newVal, builtInVal, "", null); }
	public KeyBindModifierHotKey(String keyNameIn, KeyComboAction keysIn, KeyBinding mcKeyIn, boolean newVal, String descriptionIn) { this(keyNameIn, keysIn, mcKeyIn, newVal, false, descriptionIn, null); }
	public KeyBindModifierHotKey(String keyNameIn, KeyComboAction keysIn, KeyBinding mcKeyIn, boolean newVal, boolean builtInVal, String descriptionIn, String builtInSubModTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.MC_KEYBIND_MODIFIER, builtInSubModTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		keyBind = mcKeyIn;
		val = newVal;
	}
	
	public KeyBinding getKeyBinding() { return keyBind; }
	public boolean getNewVal() { return val; }
	
	@Override
	public void executeHotKeyAction() {
		for (KeyBinding k : mc.gameSettings.keyBindings) {
			if (k.equals(keyBind)) {
				k.setKeyBindState(k.getKeyCode(), val);
				return;
			}
		}
		System.out.println("GAME SETTINGS DOES NOT CONTAIN KEY!~!~");
	}
	
	@Override
	public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + keyBind.toString());
		base += ("; " + val);
		return base;
	}
}
