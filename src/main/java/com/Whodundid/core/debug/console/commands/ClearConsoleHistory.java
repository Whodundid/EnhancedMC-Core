package com.Whodundid.core.debug.console.commands;

import com.Whodundid.core.debug.console.gui.EConsole;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ClearConsoleHistory implements IConsoleCommand {
	
	@Override public String getName() { return "clearhistory"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("clearh", "clrh"); }
	@Override public String getCommandHelpInfo() { return "Clears the typed console command history"; }
	@Override public String getCommandErrorInfo(String arg) { return null; }
	
	@Override
	public void runCommand(EConsole conIn, EArrayList<String> args, boolean runVisually) {
		conIn.clearHistory();
		conIn.writeln("history cleared..", 0x55ff55);
	}
}
