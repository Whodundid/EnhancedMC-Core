package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.hotkeys.control.Hotkey;
import com.Whodundid.hotkeys.control.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import net.minecraft.util.EnumChatFormatting;

//Last edited: Feb 18, 2019
//First Added: Feb 18, 2019
//Author: Hunter Bragg

public class KeyCategoryDeactivatorHotkey extends Hotkey {
	
	public String keyCategory;
	
	public KeyCategoryDeactivatorHotkey(String keyNameIn, KeyComboAction keysIn, String categoryNameIn) { this(keyNameIn, keysIn, categoryNameIn, false, "", null); }
	public KeyCategoryDeactivatorHotkey(String keyNameIn, KeyComboAction keysIn, String categoryNameIn, boolean builtInVal) { this(keyNameIn, keysIn, categoryNameIn, builtInVal, "", null); }
	public KeyCategoryDeactivatorHotkey(String keyNameIn, KeyComboAction keysIn, String categoryNameIn, String descriptionIn) { this(keyNameIn, keysIn, categoryNameIn, false, descriptionIn, null); }
	public KeyCategoryDeactivatorHotkey(String keyNameIn, KeyComboAction keysIn, String categoryNameIn, boolean builtInVal, String descriptionIn, String builtInAppTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.CATEGORY_DEACTIVATOR, builtInAppTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		keyCategory = categoryNameIn;
	}
	
	public String getCategoryName() { return keyCategory; }
	public KeyCategoryDeactivatorHotkey setCategoryName(String categoryNameIn) { keyCategory = categoryNameIn; return this; }
	
	@Override
	public void executeHotKeyAction() {
		EArrayList<Hotkey> foundKeys = new EArrayList();
		for (Hotkey k : man.getRegisteredHotKeys()) {
			if (k.getKeyCategory() != null && k.getKeyCategory().getCategoryName().equals(keyCategory)) { foundKeys.add(k); }
		}
		for (Hotkey k : foundKeys) { k.setEnabled(false); }
		EChatUtil.show(EnumChatFormatting.RED + "Hokeys Disabled Keys: " + keyCategory);
	}
	
	@Override
	public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + keyCategory);
		return base;
	}
	
}
