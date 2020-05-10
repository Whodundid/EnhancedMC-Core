package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.Minecraft;

//Author: Hunter Bragg

public class OpControl extends TerminalCommand {
	
	public OpControl() {
		super(CommandType.NORMAL);
		numArgs = 0;
	}

	@Override public String getName() { return "opme"; }
	@Override public boolean showInHelp() { return false; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles the state of Op mode in EMC"; }
	@Override public String getUsage() { return null; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() == 1) {
			if (args.get(0).equals("a7234fe89yx")) {
				EnhancedMC.setOpMode(!EnhancedMC.isOpMode());
				EnhancedMC.getTerminalHandler().reregisterAllCommands(false);
				termIn.getHeader().setTitle("EMC Terminal" + (EnhancedMC.isOpMode() ? " +" : ""));
				termIn.writeln(EnhancedMC.isOpMode() ? "Enabled EMC Op Mode" : "Disabled EMC Op Mode", EColors.seafoam);
			}
			else {
				termIn.error("Unrecognized command.");
			}
		}
		else {
			try {
				String id = Minecraft.getMinecraft().getSession().getProfile().getId().toString();
				if (id.equals("be8ba059-2644-4f4c-a5e7-88a38e555b1e") || id.equals("e8f9070f-74f0-4229-8134-5857c794e44d")) {
					EnhancedMC.setOpMode(!EnhancedMC.isOpMode());
					EnhancedMC.getTerminalHandler().reregisterAllCommands(false);
					termIn.getHeader().setTitle("EMC Terminal" + (EnhancedMC.isOpMode() ? " +" : ""));
					termIn.writeln(EnhancedMC.isOpMode() ? "Enabled EMC Op Mode" : "Disabled EMC Op Mode", EColors.seafoam);
				}
				else if (EnhancedMC.isOpMode()) {
					EnhancedMC.setOpMode(false);
					EnhancedMC.getTerminalHandler().reregisterAllCommands(false);
					termIn.getHeader().setTitle("EMC Terminal" + (EnhancedMC.isOpMode() ? " +" : ""));
					termIn.writeln(EnhancedMC.isOpMode() ? "Enabled EMC Op Mode" : "Disabled EMC Op Mode", EColors.seafoam);
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
