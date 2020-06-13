package com.Whodundid.core.terminal.terminalCommand.commands.hypixel;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.hypixel.HypixelData;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.util.EnumChatFormatting;

public class HypixelDataCMD extends TerminalCommand {
	
	public HypixelDataCMD() {
		super(CommandType.NORMAL);
		setCategory("Hypixel");
		numArgs = 0;
	}

	@Override public String getName() { return "hypixeldata"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("hdata"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Returns data on current hypixel server info."; }
	@Override public String getUsage() { return "ex: hdata"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (EnhancedMC.isHypixel()) {
			if (args.isEmpty()) {
				HypixelData data = EnhancedMC.getHypixelData();
				if (data != null) {
					termIn.writeln("Current Hypixel Server Data:", EColors.cyan);
					termIn.writeln("Server: " + EnumChatFormatting.GRAY + data.getServer(), EColors.lime);
					termIn.writeln("GameType: " + EnumChatFormatting.GRAY + data.getGameTypeString(), EColors.lime);
					termIn.writeln("Mode: " + EnumChatFormatting.GRAY + data.getMode(), EColors.lime);
					termIn.writeln("Map: " + EnumChatFormatting.GRAY + data.getMap(), EColors.lime);
					termIn.writeln("Is PVP: " + EnumChatFormatting.GRAY + data.isPvp(), EColors.lime);
					termIn.writeln("Is Lobby: " + EnumChatFormatting.GRAY + data.isLobby(), EColors.lime);
				}
				else { termIn.error("Data is null!"); }
			}
			else { termIn.error("This command takes no arguments!"); }
		}
		else { termIn.error("Not connected to mc.hypixel.net!"); }
	}
	
}
