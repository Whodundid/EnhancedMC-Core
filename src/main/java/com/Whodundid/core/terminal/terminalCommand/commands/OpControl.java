package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class OpControl implements IConsoleCommand {

	@Override public String getName() { return "givemethepower"; }
	@Override public boolean showInHelp() { return false; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles the state of Op mode in EMC"; }
	@Override public String getUsage() { return null; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() != 0) { conIn.error("Unrecognized command."); }
		else {
			EnhancedMC.setOpMode(!EnhancedMC.isOpMode());
			EnhancedMC.getTerminalHandler().reregisterAllCommands(false);
			conIn.getHeader().setTitle("EMC Terminal" + (EnhancedMC.isOpMode() ? " +" : ""));
			conIn.writeln(EnhancedMC.isOpMode() ? "Enabled EMC Op Mode" : "Disabled EMC Op Mode", EColors.seafoam);
		}
	}
}
