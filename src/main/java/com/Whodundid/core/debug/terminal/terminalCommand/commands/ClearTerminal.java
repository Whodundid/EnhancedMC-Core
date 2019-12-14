package com.Whodundid.core.debug.terminal.terminalCommand.commands;

import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.debug.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ClearTerminal implements IConsoleCommand {

	@Override public String getName() { return "clear"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("clr", "cls"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the console"; }
	@Override public String getUsage() { return "ex: clr"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		conIn.clear();
	}
}
