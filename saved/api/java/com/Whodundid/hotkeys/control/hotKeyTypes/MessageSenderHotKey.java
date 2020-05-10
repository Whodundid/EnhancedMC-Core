package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class MessageSenderHotKey extends HotKey {
	
	public String message = "/";
	
	public MessageSenderHotKey(String keyNameIn, KeyComboAction keysIn, String messageIn) { this(keyNameIn, keysIn, messageIn, false, "", null); }
	public MessageSenderHotKey(String keyNameIn, KeyComboAction keysIn, String messageIn, boolean builtInVal) { this(keyNameIn, keysIn, messageIn, builtInVal, "", null); }
	public MessageSenderHotKey(String keyNameIn, KeyComboAction keysIn, String messageIn, String descriptionIn) { this(keyNameIn, keysIn, messageIn, false, descriptionIn, null); }
	public MessageSenderHotKey(String keyNameIn, KeyComboAction keysIn, String messageIn, boolean builtInVal, String descriptionIn, String builtInSubModTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.MESSAGESENDER, builtInSubModTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		message = messageIn;
	}
	
	public String getMessage() { return message; }
	public MessageSenderHotKey setMessage(String messageIn) { message = messageIn; return this; }
	
	@Override public void executeHotKeyAction() { if (mc.thePlayer != null) { mc.thePlayer.sendChatMessage(message); } }
	
	@Override
	public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + message);
		return base;
	}
}
