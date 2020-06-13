package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.Minecraft;

//Author: Hunter Bragg

public class Say extends TerminalCommand {
	
	public Say() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "say"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("speak", "send", "msg"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Sends a chat message."; }
	@Override public String getUsage() { return "ex: say Hello World!"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
			if (Minecraft.getMinecraft().thePlayer != null) {
				String msg = "";
				for (int i = 0; i < args.size(); i++) {
					msg += args.get(i) + (i < args.size() - 1 ? " " : "");
				}
				EChatUtil.sendLongerChatMessage(msg);
			}
			else { termIn.error("Unknown Error!"); }
		}
		else { termIn.error("Message should not be empty!"); }
	}
	
}
