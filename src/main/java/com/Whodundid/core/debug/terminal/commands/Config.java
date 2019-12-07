package com.Whodundid.core.debug.terminal.commands;

import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class Config implements IConsoleCommand {

	@Override public String getName() { return "config"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("cfig"); }
	@Override public String getCommandHelpInfo() { return "Used to interface with EMC submod config files. (reload, save)"; }
	
	@Override
	public String getCommandErrorInfo(String arg) {
		if (arg != null) {
			switch (arg) {
			case "noarg": return "ex. config reload core";
			case "arg": return "Too many arguments!";
			}
		}
		return "Unknown error!";
	}
	
	@Override
	public EArrayList<String> getTabCompleteList() {
		return null;
	}
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { conIn.writeln(getCommandErrorInfo(""), 0xffff00); }
		else if (args.size() > 2) { conIn.error(getCommandErrorInfo("arg")); }
		else {
			switch (args.get(0)) {
			case "r":
			case "rel":
			case "reload":
			case "l":
			case "load": loadConfig(conIn, args, runVisually); break;
			case "s":
			case "save": saveConfig(conIn, args, runVisually); break;
			}
		}
	}
	
	private void loadConfig(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		//totally finished
	}
	
	private void saveConfig(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		//totally finished
	}
}
