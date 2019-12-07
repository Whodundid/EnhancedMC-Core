package com.Whodundid.core.debug.terminal.commands;

import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ClearTerminalHistory implements IConsoleCommand {
	
	@Override public String getName() { return "clearhistory"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("clearh", "clrh"); }
	@Override public String getCommandHelpInfo() { return "Clears the typed console command history"; }
	@Override public String getCommandErrorInfo(String arg) { return null; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		conIn.clearHistory();
		conIn.writeln("terminal history cleared..", 0x55ff55);
	}
}
