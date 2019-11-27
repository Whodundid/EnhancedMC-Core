package com.Whodundid.core.debug.console.commands;

import com.Whodundid.core.debug.console.gui.EConsole;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ClearConsole implements IConsoleCommand {

	@Override public String getName() { return "clear"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("clr", "cls"); }
	@Override public String getCommandHelpInfo() { return "Clears the console"; }
	@Override public String getCommandErrorInfo(String arg) { return null; }
	
	@Override
	public void runCommand(EConsole conIn, EArrayList<String> args, boolean runVisually) {
		conIn.clear();
	}
}
