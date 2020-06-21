package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.core.util.playerUtil.PlayerTraits;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;

//Last edited: Sep 30, 2018
//First Added: Sep 25, 2018
//Author: Hunter Bragg

public class ConditionalCommandSenderHotKey extends HotKey {
	
	String command = "/";
	int itemID = -1;
	
	public ConditionalCommandSenderHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, int checkItemIDIn) { this(keyNameIn, keysIn, commandIn, checkItemIDIn, false, "", null); }
	public ConditionalCommandSenderHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, int checkItemIDIn, boolean builtInVal) { this(keyNameIn, keysIn, commandIn, checkItemIDIn, builtInVal, "", null); }
	public ConditionalCommandSenderHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, int checkItemIDIn, String descriptionIn) { this(keyNameIn, keysIn, commandIn, checkItemIDIn, false, descriptionIn, null); }
	public ConditionalCommandSenderHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, int checkItemIDIn, boolean builtInVal, String descriptionIn, String builtInAppTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.CONDITIONAL_COMMAND_ITEMTEST, builtInAppTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		command = commandIn;
		itemID = checkItemIDIn;
	}
	
	public String getCommand() { return command; }
	public int getItemID() { return itemID; }
	public ConditionalCommandSenderHotKey setCommand(String commandIn) {
		command = commandIn;
		if (command == null) { command = "/"; }
		if (!command.startsWith("/")) { command = "/" + command; }
		return this;
	}
	public ConditionalCommandSenderHotKey setItemID(int idIn) { itemID = idIn; return this; }
	
	@Override
	public void executeHotKeyAction() {
		if (mc.thePlayer != null && PlayerTraits.isHoldingItem() && PlayerTraits.getHeldItemId() == itemID) {
			if (command == null) { command = "/"; }
			if (!command.startsWith("/")) { command = "/" + command; }
			mc.thePlayer.sendChatMessage(command);
		}
	}
	
	@Override
	public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + command);
		base += ("; " + itemID);
		return base;
	}
	
}
