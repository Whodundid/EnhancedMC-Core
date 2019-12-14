package com.Whodundid.core.debug.terminal.terminalCommand.commands;

import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.debug.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class Config implements IConsoleCommand {

	@Override public String getName() { return "config"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("cfig"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Used to interface with EMC submod config files. (reload, save)"; }
	@Override public String getUsage() { return "ex: config core reload"; }
	
	@Override
	public EArrayList<String> getTabCompleteList() {
		return null;
	}
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { conIn.info(getUsage()); }
		else if (args.size() > 2) { conIn.error("Too many arguments!"); }
		else {
			SubMod mod = RegisteredSubMods.getModByAlias(args.get(0));
			if (mod != null) {
				switch (args.get(1)) {
				case "r":
				case "rel":
				case "reload":
				case "l":
				case "load": loadConfig(conIn, args, runVisually, mod); break;
				case "s":
				case "save": saveConfig(conIn, args, runVisually, mod); break;
				}
			}
			else { conIn.error("No mod by that name found!"); }
		}
	}
	
	private void loadConfig(ETerminal conIn, EArrayList<String> args, boolean runVisually, SubMod modIn) {
		conIn.writeln("Reloading " + modIn.getName() + " config..", EColors.cyan);
		if (modIn.getConfig().loadAllConfigs()) { conIn.writeln("Success", EColors.green); }
		else { conIn.writeln("Failed to reload config!", EColors.red); }
	}
	
	private void saveConfig(ETerminal conIn, EArrayList<String> args, boolean runVisually, SubMod modIn) {
		conIn.writeln("Saving " + modIn.getName() + " config..", EColors.cyan);
		if (modIn.getConfig().saveAllConfigs()) { conIn.writeln("Success", EColors.green); }
		else { conIn.writeln("Failed to save config!", EColors.red); }
	}
}
