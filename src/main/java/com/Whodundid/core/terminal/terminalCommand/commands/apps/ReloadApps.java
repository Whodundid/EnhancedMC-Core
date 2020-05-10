package com.Whodundid.core.terminal.terminalCommand.commands.apps;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppLoader;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ReloadApps extends TerminalCommand {
	
	public ReloadApps() {
		super(CommandType.NORMAL);
	}

	@Override public String getName() { return "reloadapps"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("rapps"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Reloads EMC Apps"; }
	@Override public String getUsage() { return "ex: rapps"; }
	@Override public void handleTabComplete(ETerminal conIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) { termIn.info("This command does not take any arguments"); }
		else {
			if (EnhancedMC.isInitialized()) {
				RegisteredApps.unregisterApp(RegisteredApps.getAppsList());
				reload(termIn, runVisually);
			}
		}
	}
	
	private void reload(ETerminal termIn, boolean runVisually) {
		AppLoader.loadApps(termIn, runVisually);
		termIn.setInputEnabled(true);
		termIn.writeln("Apps reloaded!", EColors.yellow);
	}
	
}