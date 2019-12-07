package com.Whodundid.core.debug.terminal.commands;

import com.Whodundid.core.debug.DebugFunctions;
import com.Whodundid.core.debug.IDebugCommand;
import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class DebugCommandRunner implements IConsoleCommand {
	
	@Override public String getName() { return "debc"; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getCommandHelpInfo() { return "Runs a specified built-in EMC debug command."; }
	@Override public String getCommandErrorInfo(String arg) { return "Too many arguments!"; }
	
	@Override
	public EArrayList<String> getTabCompleteList() {
		return null;
	}
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() > 1) { conIn.error(getCommandErrorInfo(null)); }
		if (args.size() == 1) {
			try {
				int v = Integer.parseInt(args.get(0));
				if (v < 0 || v > 3) { conIn.error("Command number" + args.get(0) + " out of range!"); }
				else {
					conIn.writeln("Running debug " + v, 0x55ff55);
					DebugFunctions.runDebugFunction(v);
				}
			} catch (Exception e) {
				e.printStackTrace();
				conIn.error("Unexpected error..");
			}
		}
		else if (args.size() == 0) {
			conIn.writeln("Running debug " + 0, 0x55ff55);
			DebugFunctions.runDebugFunction(IDebugCommand.DEBUG_0);
		}
		else { conIn.error(getCommandErrorInfo(null)); }
	}
}
