package com.Whodundid.core.debug.terminal.terminalCommand.commands;

import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.debug.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.util.SubModEnabler;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class DisableMod implements IConsoleCommand {
	
	//mult dep don't work
	
	@Override public String getName() { return "disable"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("dis"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Attempts to disable a specified EMC submod"; }
	@Override public String getUsage() { return "ex: dis chat"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { conIn.info(getUsage()); }
		if (args.size() >= 1) {
			for (String s : args) {
				SubMod m = RegisteredSubMods.getModByAlias(s);
				if (m != null) {
					if (!m.isEnabled()) { conIn.writeln(m.getName() + " is already disabled.", EColors.orange); }
					else {
						if (SubModEnabler.disableMod(m, conIn)) { conIn.writeln(m.getName() + " disabled", EColors.green); }
					}
				}
				else { conIn.error("Cannot find a submod by '" + s + "'"); }
			}
		}
	}
}