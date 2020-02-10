package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.ITerminalCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.Minecraft;

//Author: Hunter Bragg

public class WhoAmI implements ITerminalCommand {

	@Override public String getName() { return "whoami"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Provides user info on the current player."; }
	@Override public String getUsage() { return null; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }

	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			try {
				Minecraft mc = Minecraft.getMinecraft();
				String player = mc.getSession().getUsername();
				String uuid = mc.getSession().getProfile().getId().toString();
				String type = uuid.equals("be8ba059-2644-4f4c-a5e7-88a38e555b1e") || uuid.equals("e8f9070f-74f0-4229-8134-5857c794e44d") ? "<Dev" : "<User";
				if (EnhancedMC.isOpMode()) { type += ", Op"; }
				type += ">";

				conIn.writeln("Username: " + player, EColors.green);
				conIn.writeln("Groups: " + type, EColors.cyan);
				if (runVisually) { conIn.writeln("UUID: " + uuid, EColors.yellow); }
			}
			catch (Exception e) {
				e.printStackTrace();
				conIn.badError(e.toString());
			}
		}
		else { conIn.error("This command does not take arguments!"); }
	}
}
