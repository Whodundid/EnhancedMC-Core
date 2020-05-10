package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class ClearTerminal extends TerminalCommand {
	
	public ClearTerminal() {
		super(CommandType.NORMAL);
		numArgs = 0;
	}

	@Override public String getName() { return "clear"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("clr", "cls"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the terminal"; }
	@Override public String getUsage() { return "ex: clr"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.clear();
	}
}
