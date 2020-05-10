package com.Whodundid.playerInfo.term;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.playerInfo.PlayerInfoApp;

public class Term_NameHistory extends TerminalCommand {
	
	PlayerInfoApp mod = (PlayerInfoApp) RegisteredApps.getApp(AppType.PLAYERINFO);
	
	public Term_NameHistory() {
		super(CommandType.MOD_COMMAND);
	}

	@Override public String getName() { return "names"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Fetches a player's past name history"; }
	@Override public String getUsage() { return "ex: names notch"; }
	@Override public void handleTabComplete(ETerminal conIn, EArrayList<String> args) {}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			termIn.error("Not enough arguments!");
			termIn.info(getUsage());
		}
		else if (args.size() == 1) {
			mod.fetchNameHistory(termIn, args.get(0), false);
		}
		else { termIn.error("Too many arguments!"); }
	}

}
