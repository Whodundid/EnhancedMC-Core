package com.Whodundid.core.debug.terminal.terminalCommand.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.debug.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ReregisterCommands implements IConsoleCommand {
	
	@Override public String getName() { return "reregisterallcommands"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList("rrac", "reloadcommands", "reloadcmds"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Rebuilds the commands in the console's command handler."; }
	@Override public String getUsage() { return "ex: rrac -v"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		conIn.writeln("Reregistering all commands..", 0xffaa00);
		EnhancedMC.getTerminalHandler().reregisterAllCommands(conIn, runVisually);
	}
}
