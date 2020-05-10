package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class ClearTerminalHistory extends TerminalCommand {
	
	public ClearTerminalHistory() {
		super(CommandType.NORMAL);
		numArgs = 0;
	}
	
	@Override public String getName() { return "clearhistory"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("clearh", "clrh"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the typed terminal command history"; }
	@Override public String getUsage() { return "ex: clrh"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		EnhancedMC.getTerminalHandler().clearHistory();
		termIn.writeln("Terminal history cleared..", 0x55ff55);
	}
}
