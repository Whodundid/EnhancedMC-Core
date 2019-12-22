package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ClearTerminalHistory implements IConsoleCommand {
	
	@Override public String getName() { return "clearhistory"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("clearh", "clrh"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the typed console command history"; }
	@Override public String getUsage() { return "ex: clrh"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		conIn.clearHistory();
		conIn.writeln("terminal history cleared..", 0x55ff55);
	}
}
