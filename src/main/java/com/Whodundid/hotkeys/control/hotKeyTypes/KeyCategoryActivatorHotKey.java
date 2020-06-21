package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import net.minecraft.util.EnumChatFormatting;

//Last edited: Feb 18, 2019
//First Added: Feb 18, 2019
//Author: Hunter Bragg

public class KeyCategoryActivatorHotKey extends HotKey {
	
	public String keyCategory;
	
	public KeyCategoryActivatorHotKey(String keyNameIn, KeyComboAction keysIn, String categoryNameIn) { this(keyNameIn, keysIn, categoryNameIn, false, "", null); }
	public KeyCategoryActivatorHotKey(String keyNameIn, KeyComboAction keysIn, String categoryNameIn, boolean builtInVal) { this(keyNameIn, keysIn, categoryNameIn, builtInVal, "", null); }
	public KeyCategoryActivatorHotKey(String keyNameIn, KeyComboAction keysIn, String categoryNameIn, String descriptionIn) { this(keyNameIn, keysIn, categoryNameIn, false, descriptionIn, null); }
	public KeyCategoryActivatorHotKey(String keyNameIn, KeyComboAction keysIn, String categoryNameIn, boolean builtInVal, String descriptionIn, String builtInAppTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.CATEGORY_ACTIVATOR, builtInAppTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		keyCategory = categoryNameIn;
	}
	
	public String getCategoryName() { return keyCategory; }
	public KeyCategoryActivatorHotKey setCategoryName(String categoryNameIn) { keyCategory = categoryNameIn; return this; }
	
	@Override
	public void executeHotKeyAction() {
		EArrayList<HotKey> foundKeys = new EArrayList();
		for (HotKey k : man.getRegisteredHotKeys()) {
			if (k.getKeyCategory() != null && k.getKeyCategory().getCategoryName().equals(keyCategory)) { foundKeys.add(k); }
		}
		for (HotKey k : foundKeys) { k.setEnabled(true); }
		EChatUtil.show(EnumChatFormatting.GREEN + "Hokeys Enabled Keys: " + keyCategory);
	}
	
	@Override
	public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + keyCategory);
		return base;
	}
	
}
