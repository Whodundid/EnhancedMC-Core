package com.Whodundid.core.debug.terminal.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.debug.terminal.TerminalCommandHandler;
import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ReregisterCommands implements IConsoleCommand {
	
	TerminalCommandHandler handler;
	
	@Override public String getName() { return "reregisterallcommands"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList("rrac", "reload"); }
	@Override public String getCommandHelpInfo() { return "Rebuilds the commands in the console's command handler."; }
	@Override public String getCommandErrorInfo(String arg) { return null; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		handler = EnhancedMC.getTerminal();
		conIn.writeln("Reregistering all commands..", 0xffaa00);
		handler.reregisterAllCommands(conIn, runVisually);
	}
}
