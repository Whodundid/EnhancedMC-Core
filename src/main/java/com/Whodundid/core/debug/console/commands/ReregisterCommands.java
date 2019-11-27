package com.Whodundid.core.debug.console.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.debug.console.ConsoleCommandHandler;
import com.Whodundid.core.debug.console.gui.EConsole;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ReregisterCommands implements IConsoleCommand {
	
	ConsoleCommandHandler handler;
	
	@Override public String getName() { return "reregisterallcommands"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList("rrac", "reload"); }
	@Override public String getCommandHelpInfo() { return "Rebuilds the commands in the console's command handler."; }
	@Override public String getCommandErrorInfo(String arg) { return null; }
	
	@Override
	public void runCommand(EConsole conIn, EArrayList<String> args, boolean runVisually) {
		handler = EnhancedMC.getConsole();
		conIn.writeln("Reregistering all commands..", 0xffaa00);
		handler.reregisterAllCommands(conIn, runVisually);
	}
}
