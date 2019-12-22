package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.Minecraft;

public class Say implements IConsoleCommand {

	@Override public String getName() { return "say"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("speak", "send", "msg"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Sends a chat message."; }
	@Override public String getUsage() { return "ex: say Hello World!"; }
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
		} else { conIn.error("Message should not be empty!"); }
	}
}
