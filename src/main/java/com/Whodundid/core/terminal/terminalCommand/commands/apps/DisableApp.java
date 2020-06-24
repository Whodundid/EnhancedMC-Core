package com.Whodundid.core.terminal.terminalCommand.commands.apps;

import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.app.util.AppEnabler;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class DisableApp extends TerminalCommand {
	
	public DisableApp() {
		super(CommandType.NORMAL);
		setCategory("App Specific");
		numArgs = 1;
	}
	
	@Override public String getName() { return "disable"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("dis"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Attempts to disable a specified EMC App"; }
	@Override public String getUsage() { return "ex: dis core"; }

	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		int arg = termIn.getCurrentArg() - 1;
		
		switch (arg) {
		case 0:
			if (!termIn.getTab1()) {
				EArrayList<String> names = new EArrayList("all");
				EArrayList<String> completions = new EArrayList();
				
				String curArg = args.get(arg);
				
				for (EMCApp m : RegisteredApps.getAppsList()) {
					if (m.getNameAliases().isNotEmpty()) {
						String name = m.getNameAliases().get(m.getNameAliases().size() - 1);
						names.add(name);
					}
				}
				
				for (String s : names) {
					if (s.startsWith(curArg)) { completions.add(s); }
				}
				
				termIn.buildTabCompletions(completions);
			}
			break;
		}
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.info(getUsage()); }
		if (args.size() >= 1) {
			for (String s : args) {
				if (s.equals("all")) {

					for (EMCApp m : RegisteredApps.getRegisteredAppsList()) {
						if (!m.isEnabled()) { termIn.writeln(m.getName() + " is already disabled.", EColors.orange); }
						else {
							if (AppEnabler.disableApp(m, termIn)) {
								termIn.writeln(m.getName() + " disabled", EColors.green);
							}
						}
					}
					
					break;
				}
				else {
					EMCApp m = RegisteredApps.getAppByAlias(s);
					if (m != null) {
						if (!m.isEnabled()) { termIn.writeln(m.getName() + " is already disabled.", EColors.orange); }
						else {
							if (AppEnabler.disableApp(m, termIn)) {
								termIn.writeln(m.getName() + " disabled", EColors.green);
							}
						}
					}
					else { termIn.error("Cannot find an app by '" + s + "'"); }
				}
			}
		} //if
	}
	
}
