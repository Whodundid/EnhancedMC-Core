package com.Whodundid.core.debug.terminal.commands;

import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.Minecraft;

public class Say implements IConsoleCommand {

	@Override public String getName() { return "say"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("speak", "send", "msg"); }
	@Override public String getCommandHelpInfo() { return "Sends a chat message."; }
	@Override public String getCommandErrorInfo(String arg) { return "Message should not be empty!"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
			if (Minecraft.getMinecraft().thePlayer != null) {
				String msg = "";
				for (int i = 0; i < args.size(); i++) {
					msg += args.get(i) + (i < args.size() - 1 ? " " : "");
				}
				EChatUtil.sendLongerChatMessage(msg);
			} else { conIn.error("Unknown Error!"); }
		} else { conIn.error(getCommandErrorInfo(null)); }
	}
}
