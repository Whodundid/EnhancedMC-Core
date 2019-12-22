package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ToggleSafeRM implements IConsoleCommand {

	@Override public String getName() { return "saferemotedesktop"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("srm"); }
	
	@Override public String getHelpInfo(boolean runVisually) {
		return "Toggles safe remote desktop mode for EMC." + (runVisually ? " Disables cursor image changes." : "");
	}
	
	@Override public String getUsage() { return ""; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() > 0) { conIn.error("This command does not take arguments"); }
		else {
			EnhancedMC.safeRemoteDesktopMode = !EnhancedMC.safeRemoteDesktopMode;
			conIn.writeln(EnhancedMC.safeRemoteDesktopMode ? "Enabled Safe Remote Desktop Mode" : "Disabled Safe Remote Desktop Mode", 0xaabbcc);
		}
	}
}
