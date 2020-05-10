package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class ReregisterCommands extends TerminalCommand {
	
	public ReregisterCommands() {
		super(CommandType.NORMAL);
		numArgs = 0;
	}
	
	@Override public String getName() { return "reregisterallcommands"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList("rrac", "reloadcommands", "reloadcmds"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Rebuilds the commands in the terminal's command handler."; }
	@Override public String getUsage() { return "ex: rrac -i"; }
	@Override public void handleTabComplete(ETerminal conIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		conIn.writeln("Reregistering all commands..", 0xffaa00);
		EnhancedMC.getTerminalHandler().reregisterAllCommands(conIn, runVisually);
	}
}
