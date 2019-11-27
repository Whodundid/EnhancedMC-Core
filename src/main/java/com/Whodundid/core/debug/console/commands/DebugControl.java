package com.Whodundid.core.debug.console.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.debug.console.gui.EConsole;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class DebugControl implements IConsoleCommand {

	@Override public String getName() { return "debug"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("deb"); }
	@Override public String getCommandHelpInfo() { return "Toggles debug mode for EMC."; }
	@Override public String getCommandErrorInfo(String arg) { return "This command does not take arguments"; }
	
	@Override
	public void runCommand(EConsole conIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() > 0) { conIn.writeln(getCommandErrorInfo(null), 0xff5555); }
		else {
			EnhancedMC.setDebugMode(!EnhancedMC.isDebugMode());
			conIn.writeln(EnhancedMC.isDebugMode() ? "Enabled Debug Mode" : "Disabled Debug Mode", 0xaabbcc);
		}
	}
}
