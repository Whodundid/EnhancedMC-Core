package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class Exit implements IConsoleCommand {

	@Override public String getName() { return "exit"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("close"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Closes the terminal"; }
	@Override public String getUsage() { return "ex: exit"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		conIn.close();
	}
}
