package com.Whodundid.core.debug.terminal.commands;

import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ClearTerminal implements IConsoleCommand {

	@Override public String getName() { return "clear"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("clr", "cls"); }
	@Override public String getCommandHelpInfo() { return "Clears the console"; }
	@Override public String getCommandErrorInfo(String arg) { return null; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		conIn.clear();
	}
}
