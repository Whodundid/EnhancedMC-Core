package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.Minecraft;

//Author: Hunter Bragg

public class WhoAmI extends TerminalCommand {
	
	public WhoAmI() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "whoami"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Provides user info on the current player."; }
	@Override public String getUsage() { return null; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {}

	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			try {
				Minecraft mc = Minecraft.getMinecraft();
				String player = mc.getSession().getUsername();
				String uuid = mc.getSession().getProfile().getId().toString();
				String type = uuid.equals("be8ba059-2644-4f4c-a5e7-88a38e555b1e") || uuid.equals("e8f9070f-74f0-4229-8134-5857c794e44d") ? "<Dev" : "<User";
				if (EnhancedMC.isDevMode()) { type += ", Op"; }
				type += ">";

				termIn.writeln("Username: " + player, EColors.green);
				termIn.writeln("UUID: " + uuid, EColors.yellow);
				termIn.writeln("EMC Groups: " + type, EColors.cyan);
			}
			catch (Exception e) {
				error(termIn, e);
			}
		}
		else { termIn.error("This command does not take arguments!"); }
	}
	
}
