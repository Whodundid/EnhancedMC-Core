package com.Whodundid.hotkeys.control.hotKeyUtil;

import com.Whodundid.core.util.storageUtil.EArrayList;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

/*
 * Holds a variable amount of lwjgl.Keyboard keyCodes used to trigger an event.
 * Additional modifier keys can be set which include Ctrl, Shift, and Alt.
 */
public class KeyComboAction {
	
	public EArrayList<Integer> hotKeyCodes = new EArrayList();
	
	/**
	 * Takes in a variable amount of lwjgl.Keyboard keyCodes used to trigger an event.
	 * @param codeIn {@code Integer[]}
	 */
	public KeyComboAction(int... codeIn) {
		setKeys(codeIn);
	}
	
	public void setKeys(int... codeIn) {
		synchronized (hotKeyCodes) {
			hotKeyCodes.clear();
			for (int i : codeIn) { hotKeyCodes.add(i); }
		}
	}
	
	public boolean checkKeys(int[] keys) { return checkKeys(new EArrayList(keys)); }
	
	public boolean checkKeys(EArrayList<Integer> checkKeys) {
		if (hotKeyCodes.size() == checkKeys.size()) {
			boolean pass = true;
			for (int i : hotKeyCodes) {
				if (!checkKeys.contains(i)) { pass = false; }
			}
			return pass;
		}
		return false;
	}
	
	public EArrayList<Integer> getKeys() { return hotKeyCodes; }
}
