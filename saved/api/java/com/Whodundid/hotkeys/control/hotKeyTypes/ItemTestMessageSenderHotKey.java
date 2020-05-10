package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.core.util.playerUtil.PlayerTraits;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;

//Last edited: Sep 30, 2018
//First Added: Sep 25, 2018
//Author: Hunter Bragg

public class ItemTestMessageSenderHotKey extends HotKey {
	
	String message = "";
	int itemID = -1;
	
	public ItemTestMessageSenderHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, int checkItemIDIn) { this(keyNameIn, keysIn, commandIn, checkItemIDIn, false, "", null); }
	public ItemTestMessageSenderHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, int checkItemIDIn, boolean builtInVal) { this(keyNameIn, keysIn, commandIn, checkItemIDIn, builtInVal, "", null); }
	public ItemTestMessageSenderHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, int checkItemIDIn, String descriptionIn) { this(keyNameIn, keysIn, commandIn, checkItemIDIn, false, descriptionIn, null); }
	public ItemTestMessageSenderHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, int checkItemIDIn, boolean builtInVal, String descriptionIn, String builtInSubModTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.CONDITIONAL_MESSAGE_ITEMTEST, builtInSubModTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		message = commandIn;
		itemID = checkItemIDIn;
	}
	
	public String getMessage() { return message; }
	public int getItemID() { return itemID; }
	public ItemTestMessageSenderHotKey setMessage(String commandIn) { message = commandIn; return this; }
	public ItemTestMessageSenderHotKey setItemID(int idIn) { itemID = idIn; return this; }
	
	@Override
	public void executeHotKeyAction() {
		if (mc.thePlayer != null && PlayerTraits.isHoldingItem() && PlayerTraits.getHeldItemId() == itemID) { mc.thePlayer.sendChatMessage(message); }
	}
	
	@Override
	public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + message);
		base += ("; " + itemID);
		return base;
	}
}
