package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.debug.DebugFunctions;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class DebugControl implements IConsoleCommand {

	@Override public String getName() { return "debug"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("deb", "dev"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles debug mode for EMC."; }
	@Override public String getUsage() { return "ex: deb | deb 0"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() > 1) { conIn.error("Too many arguments!"); }
		if (args.size() == 1) {
			try {
				int v = Integer.parseInt(args.get(0));
				if (v < 0 || v > 3) { conIn.error("Command number " + args.get(0) + " out of range!"); }
				else {
					conIn.writeln("Running debug " + v, 0x55ff55);
					DebugFunctions.runDebugFunction(v);
				}
			} catch (Exception e) {
				e.printStackTrace();
				conIn.error("Unexpected error..");
			}
		}
		else {
			EnhancedMC.setDebugMode(!EnhancedMC.isDebugMode());
			conIn.writeln(EnhancedMC.isDebugMode() ? "Enabled Debug Mode" : "Disabled Debug Mode", 0xaabbcc);
		}
	}
}
