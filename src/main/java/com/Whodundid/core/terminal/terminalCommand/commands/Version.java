package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.terminal.TerminalCommandHandler;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.ITerminalCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.Loader;

//Author: Hunter Bragg

public class Version implements ITerminalCommand {
	
	@Override public String getName() { return "version"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("ver", "v"); }
	@Override public String getHelpInfo(boolean runVisually) { return "displays the version of the provided argument."; }
	@Override public String getUsage() { return "ex: v core"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			if (runVisually) { conIn.writeln("terminal, minecraft, minecraftforge, 'submod'", EColors.green); }
			else { conIn.writeln("EMC Terminal Version: " + TerminalCommandHandler.version, EColors.cyan); }
		}
		else if (args.size() == 1) {
			String arg = args.get(0).toLowerCase();
			switch (arg) {
			case "t":
			case "term":
			case "terminal": conIn.writeln("EMC Terminal Version: " + EnumChatFormatting.GREEN + TerminalCommandHandler.version, EColors.cyan); break;
			
			case "mc":
			case "minecraft": conIn.writeln("Minecraft Version: " + EnumChatFormatting.GREEN + Minecraft.getMinecraft().getVersion(), EColors.cyan); break;
			
			case "forge":
			case "minecraftforge": conIn.writeln("Minecraft Forge Version: " + EnumChatFormatting.GREEN + ForgeVersion.getVersion(), EColors.cyan); break;
			
			case "minecraftcoderpack":
			case "mcp": conIn.writeln("Minecraft Coder Pack Version: " + EnumChatFormatting.GREEN + ForgeVersion.mcpVersion, EColors.cyan); break;
			
			case "forgemodloader":
			case "fml": conIn.writeln("Forge Mod Loader Version: " + EnumChatFormatting.GREEN + Loader.instance().getFMLVersionString(), EColors.cyan); break;
			
			default:
				SubMod mod = RegisteredSubMods.getModByAlias(arg);
				if (mod != null) {
					conIn.writeln(mod.getName() + " Version: " + EnumChatFormatting.GREEN + mod.getVersion() + " : " + mod.getVersionDate(), EColors.cyan);
				}
				else {
					conIn.error("Unrecognized argument");
				}
			}
		}
		else {
			conIn.error("Too many arguments!");
		}
	}
}
