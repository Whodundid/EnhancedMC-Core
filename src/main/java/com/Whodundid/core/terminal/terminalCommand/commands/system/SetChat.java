package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class SetChat extends TerminalCommand {
	
	public SetChat() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 1;
	}
	
	@Override public String getName() { return "setchat"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("chat"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Converts this EMC Terminal into a chat window"; }
	@Override public String getUsage() { return "ex: setchat true"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		basicTabComplete(termIn, args, new EArrayList<String>("true", "false"));
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			termIn.setChatTerminal(!termIn.isChatTerminal());
		}
		else if (args.size() == 1) {
			try {
				boolean val = Boolean.parseBoolean(args.get(0));
				termIn.setChatTerminal(val);
			}
			catch (Exception e) {
				termIn.error("Cannot parse input!");
				termIn.info(getUsage());
				e.printStackTrace();
			}
		}
		else { termIn.error("Too many arguments!"); termIn.info(getUsage()); }
	}
	
}