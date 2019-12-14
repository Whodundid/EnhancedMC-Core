package com.Whodundid.core.debug.terminal.terminalCommand.commands;

import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.debug.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.util.SubModEnabler;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class EnableMod implements IConsoleCommand {
	
	//mult dep don't work
	
	@Override public String getName() { return "enable"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("en"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Attempts to enable a specified EMC submod"; }
	@Override public String getUsage() { return "ex: en chat"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { conIn.info(getUsage()); }
		if (args.size() >= 1) {
			EArrayList<SubMod> mods = new EArrayList();
			for (String s : args) {
				SubMod m = RegisteredSubMods.getModByAlias(s);
				if (m != null) {
					if (m.isEnabled()) { conIn.writeln(m.getName() + " is already enabled.", EColors.orange); }
					else {
						if (SubModEnabler.enableMod(m, conIn)) { conIn.writeln(m.getName() + " enabled", EColors.green); }
					}
				}
				else { conIn.error("Cannot find a submod by '" + s + "'"); }
			}
		}
	}
}
