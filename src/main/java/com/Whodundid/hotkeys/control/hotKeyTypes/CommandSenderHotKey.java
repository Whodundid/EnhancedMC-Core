package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import net.minecraftforge.client.ClientCommandHandler;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class CommandSenderHotKey extends HotKey {
	
	public String command = "/";
	
	public CommandSenderHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn) { this(keyNameIn, keysIn, commandIn, false, "", null); }
	public CommandSenderHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, boolean builtInVal) { this(keyNameIn, keysIn, commandIn, builtInVal, "", null); }
	public CommandSenderHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, String descriptionIn) { this(keyNameIn, keysIn, commandIn, false, descriptionIn, null); }
	public CommandSenderHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, boolean builtInVal, String descriptionIn, String builtInAppTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.COMMANDSENDER, builtInAppTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		command = commandIn;
	}
	
	public String getCommand() { return command; }
	
	public CommandSenderHotKey setcommand(String commandIn) {
		command = commandIn;
		if (command == null) { command = "/"; }
		if (!command.startsWith("/")) { command = "/" + command; }
		return this;
	}
	
	@Override
	public void executeHotKeyAction() {
		if (mc.thePlayer != null) {
			if (command == null) { command = "/"; }
			if (!command.startsWith("/")) { command = "/" + command; }
			
			if (ClientCommandHandler.instance.executeCommand(mc.thePlayer, command) != 0) { return; }
			mc.thePlayer.sendChatMessage(command);
		}
	}
	
	@Override
	public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + command);
		return base;
	}
	
}
