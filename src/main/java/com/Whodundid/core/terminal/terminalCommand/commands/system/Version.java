package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.terminal.TerminalCommandHandler;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.Loader;

//Author: Hunter Bragg

public class Version extends TerminalCommand {
	
	public Version() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}
	
	@Override public String getName() { return "version"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("ver", "v"); }
	@Override public String getHelpInfo(boolean runVisually) { return "displays the version of the provided argument."; }
	@Override public String getUsage() { return "ex: v core"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		EArrayList<String> types = new EArrayList("terminal", "mc", "minecraft", "forge", "minecraftforge", "minecraftcoderpack", "mcp", "forgemodloader", "fml");
		for (EMCApp a : RegisteredApps.getAppsList()) {
			for (String s : a.getNameAliases()) { types.add(s); }
		}
		
		super.basicTabComplete(termIn, args, types);
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			if (runVisually) { termIn.writeln("terminal, minecraft, minecraftforge, 'EMCApp'", EColors.green); }
			else { termIn.writeln("EMC Terminal Version: " + TerminalCommandHandler.version, EColors.cyan); }
		}
		else if (args.size() == 1) {
			String arg = args.get(0).toLowerCase();
			switch (arg) {
			case "t":
			case "term":
			case "terminal": termIn.writeln("EMC Terminal Version: " + EnumChatFormatting.GREEN + TerminalCommandHandler.version, EColors.cyan); break;
			
			case "mc":
			case "minecraft": termIn.writeln("Minecraft Version: " + EnumChatFormatting.GREEN + Minecraft.getMinecraft().getVersion(), EColors.cyan); break;
			
			case "forge":
			case "minecraftforge": termIn.writeln("Minecraft Forge Version: " + EnumChatFormatting.GREEN + ForgeVersion.getVersion(), EColors.cyan); break;
			
			case "minecraftcoderpack":
			case "mcp": termIn.writeln("Minecraft Coder Pack Version: " + EnumChatFormatting.GREEN + ForgeVersion.mcpVersion, EColors.cyan); break;
			
			case "forgemodloader":
			case "fml": termIn.writeln("Forge Mod Loader Version: " + EnumChatFormatting.GREEN + Loader.instance().getFMLVersionString(), EColors.cyan); break;
			
			default:
				EMCApp app = RegisteredApps.getAppByAlias(arg);
				if (app != null) {
					termIn.writeln(app.getName() + " Version: " + EnumChatFormatting.GREEN + app.getVersion() + " : " + app.getVersionDate(), EColors.cyan);
				}
				else {
					termIn.error("Unrecognized argument");
				}
			}
		}
		else {
			termIn.error("Too many arguments!");
		}
	}
}
