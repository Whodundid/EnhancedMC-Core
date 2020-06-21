package com.Whodundid.playerInfo.terminal;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.playerInfo.PlayerInfoApp;

public class Term_PlayerSkin extends TerminalCommand {
	
	PlayerInfoApp mod = (PlayerInfoApp) RegisteredApps.getApp(AppType.PLAYERINFO);
	
	public Term_PlayerSkin() {
		super(CommandType.APP_COMMAND);
		setCategory("Player Info");
	}

	@Override public String getName() { return "skin"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Fetches a player's current skin"; }
	@Override public String getUsage() { return "ex: skin notch"; }
	@Override public void handleTabComplete(ETerminal conIn, EArrayList<String> args) {}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (mod.isEnabled()) {
			if (args.isEmpty()) {
				termIn.error("Not enough arguments!");
				termIn.info(getUsage());
			}
			else if (args.size() == 1) {
				mod.fetchSkin(null, termIn, args.get(0));
			}
			else { termIn.error("Too many arguments!"); }
		}
		else { termIn.error("PlayerInfo App is disabled! Enable it to use."); }
	}

}
