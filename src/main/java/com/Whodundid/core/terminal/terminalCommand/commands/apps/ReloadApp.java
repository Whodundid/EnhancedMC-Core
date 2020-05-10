package com.Whodundid.core.terminal.terminalCommand.commands.apps;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppLoader;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ReloadApp extends TerminalCommand {
	
	private EMCApp app = null;
	
	public ReloadApp() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}

	@Override public String getName() { return "reloadapp"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("rapp"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Reloads a single EMC App"; }
	@Override public String getUsage() { return "ex: rapp core"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		EArrayList<String> completionsIn = new EArrayList();
		for (EMCApp a : RegisteredApps.getAppsList()) {
			completionsIn.add(a.getName());
			completionsIn.addAll(a.getNameAliases());
		}
		basicTabComplete(termIn, args, completionsIn);
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); termIn.info(getUsage()); }
		else if (args.size() == 1) {
			String arg = args.get(0).toLowerCase();
			
			
			for (EMCApp a : RegisteredApps.getAppsList()) {
				if (a.getName().toLowerCase().equals(arg)) { app = a; break; }
				
				boolean found = false;
				for (String s : a.getNameAliases()) {
					if (s.toLowerCase().equals(arg)) { app = a; found = true; break; }
				}
				
				if (found) { break; }
			}
			
			if (app != null) {
				if (EnhancedMC.isInitialized()) {
					reload(termIn, app, runVisually);
				}
			}
			else {
				termIn.error("No app found with that name!");
			}
		}
		else {
			termIn.error("Too many arguments!");
		}
	}
	
	private void reload(ETerminal termIn, EMCApp app, boolean runVisually) {
		AppLoader.reloadApp(termIn, app, true);
		termIn.setInputEnabled(true);
	}
	
}