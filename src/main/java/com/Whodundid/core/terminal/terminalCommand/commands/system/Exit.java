package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class Exit extends TerminalCommand {
	
	public Exit() {
		super(CommandType.NORMAL);
		numArgs = 0;
	}

	@Override public String getName() { return "exit"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("close"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Closes the terminal"; }
	@Override public String getUsage() { return "ex: exit"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.close();
	}
}
