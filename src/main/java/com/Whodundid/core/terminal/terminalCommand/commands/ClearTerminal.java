package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class ClearTerminal implements IConsoleCommand {

	@Override public String getName() { return "clear"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("clr", "cls"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the console"; }
	@Override public String getUsage() { return "ex: clr"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		conIn.clear();
	}
}
