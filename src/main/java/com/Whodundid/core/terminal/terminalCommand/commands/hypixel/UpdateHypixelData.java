package com.Whodundid.core.terminal.terminalCommand.commands.hypixel;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.TerminalCommandHandler;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class UpdateHypixelData extends TerminalCommand {
	
	public UpdateHypixelData() {
		super(CommandType.NORMAL);
		setCategory("Hypixel");
		numArgs = 0;
	}

	@Override public String getName() { return "updatehypixeldata"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("uhdata"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Requests an update on current hypixel server info."; }
	@Override public String getUsage() { return "ex: uhdata"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (EnhancedMC.isHypixel()) {
			if (args.isEmpty()) {
				termIn.writeln("Sending update request...", EColors.yellow);
				EnhancedMC.requestHypixelServerInfo(termIn);
				TerminalCommandHandler.drawSpace = false;
			}
			else { termIn.error("This command takes no arguments!"); }
		}
		else { termIn.error("Not connected to mc.hypixel.net!"); }
	}
	
}
