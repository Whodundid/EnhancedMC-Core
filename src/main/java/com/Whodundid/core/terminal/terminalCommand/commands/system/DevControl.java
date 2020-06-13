package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.Minecraft;

//Author: Hunter Bragg

public class DevControl extends TerminalCommand {
	
	public DevControl() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "endev"; }
	@Override public boolean showInHelp() { return false; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles the state of dev mode in EMC"; }
	@Override public String getUsage() { return null; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() == 1) {
			if (args.get(0).equals(CoreApp.debugCode)) {
				EnhancedMC.setDevMode(!EnhancedMC.isDevMode());
				EnhancedMC.getTerminalHandler().reregisterAllCommands(false);
				termIn.getHeader().setTitle("EMC Terminal" + (EnhancedMC.isDevMode() ? " +" : ""));
				termIn.writeln(EnhancedMC.isDevMode() ? "Enabled EMC Dev Mode" : "Disabled EMC Dev Mode", EColors.seafoam);
			}
			else {
				termIn.error("Unrecognized command.");
			}
		}
		else {
			try {
				String id = Minecraft.getMinecraft().getSession().getProfile().getId().toString();
				if (id.equals("be8ba059-2644-4f4c-a5e7-88a38e555b1e") || id.equals("e8f9070f-74f0-4229-8134-5857c794e44d")) {
					EnhancedMC.setDevMode(!EnhancedMC.isDevMode());
					EnhancedMC.getTerminalHandler().reregisterAllCommands(false);
					termIn.getHeader().setTitle("EMC Terminal" + (EnhancedMC.isDevMode() ? " +" : ""));
					termIn.writeln(EnhancedMC.isDevMode() ? "Enabled EMC Dev Mode" : "Disabled EMC Dev Mode", EColors.seafoam);
				}
				else if (EnhancedMC.isDevMode()) {
					EnhancedMC.setDevMode(false);
					EnhancedMC.getTerminalHandler().reregisterAllCommands(false);
					termIn.getHeader().setTitle("EMC Terminal" + (EnhancedMC.isDevMode() ? " +" : ""));
					termIn.writeln(EnhancedMC.isDevMode() ? "Enabled EMC Dev Mode" : "Disabled EMC Dev Mode", EColors.seafoam);
				}
				else { termIn.error("Unrecognized command."); }
			}
			catch (Exception e) {
				e.printStackTrace();
				termIn.error("Unrecognized command.");
			}
		}
	}
	
}
