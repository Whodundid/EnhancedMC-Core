package com.Whodundid.core.terminal.terminalCommand.commands.apps;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.app.util.AppEnabler;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class EnableApp extends TerminalCommand {
	
	public EnableApp() {
		super(CommandType.NORMAL);
		setCategory("App Specific");
		numArgs = 1;
	}
	
	@Override public String getName() { return "enable"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("en"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Attempts to enable a specified EMC submod"; }
	@Override public String getUsage() { return "ex: en core"; }
	
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
			EArrayList<EMCApp> mods = new EArrayList();
			for (String s : args) {
				if (s.equals("all")) {
					
					for (EMCApp m : RegisteredApps.getRegisteredAppList()) {
						if (!m.isEnabled()) { termIn.writeln(m.getName() + " enabled", EColors.green); }
						m.setEnabled(true);
						EnhancedMC.reloadAllWindows();
					}
					
					break;
				}
				else {
					EMCApp m = RegisteredApps.getAppByAlias(s);
					if (m != null) {
						if (m.isEnabled()) { termIn.writeln(m.getName() + " is already enabled.", EColors.orange); }
						else {
							if (AppEnabler.enableApp(m, termIn)) { termIn.writeln(m.getName() + " enabled", EColors.green); }
						}
					}
					else { termIn.error("Cannot find a submod by '" + s + "'"); }
				}
			}
		} //if
	}
	
}
