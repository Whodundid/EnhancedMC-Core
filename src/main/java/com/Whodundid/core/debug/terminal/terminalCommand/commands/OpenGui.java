package com.Whodundid.core.debug.terminal.terminalCommand.commands;

import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.debug.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class OpenGui implements IConsoleCommand {
	
	@Override public String getName() { return "open"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList("o"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Command used for opening various things."; }
	@Override public String getUsage() { return "ex: open settings"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		
	}
}
