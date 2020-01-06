package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.Minecraft;

//Author: Hunter Bragg

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
			try {
				String id = Minecraft.getMinecraft().getSession().getProfile().getId().toString();
				if (id.equals("be8ba059-2644-4f4c-a5e7-88a38e555b1e") || id.equals("e8f9070f-74f0-4229-8134-5857c794e44d")) {
					EnhancedMC.setOpMode(!EnhancedMC.isOpMode());
					EnhancedMC.getTerminalHandler().reregisterAllCommands(false);
					conIn.getHeader().setTitle("EMC Terminal" + (EnhancedMC.isOpMode() ? " +" : ""));
					conIn.writeln(EnhancedMC.isOpMode() ? "Enabled EMC Op Mode" : "Disabled EMC Op Mode", EColors.seafoam);
				}
				else if (EnhancedMC.isOpMode()) {
					EnhancedMC.setOpMode(false);
					EnhancedMC.getTerminalHandler().reregisterAllCommands(false);
					conIn.getHeader().setTitle("EMC Terminal" + (EnhancedMC.isOpMode() ? " +" : ""));
					conIn.writeln(EnhancedMC.isOpMode() ? "Enabled EMC Op Mode" : "Disabled EMC Op Mode", EColors.seafoam);
				}
				else { conIn.error("Unrecognized command."); }
			}
			catch (Exception e) {
				e.printStackTrace();
				conIn.error("Unrecognized command.");
			}
		}
	}
}
