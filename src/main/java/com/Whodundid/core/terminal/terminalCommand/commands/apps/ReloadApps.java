package com.Whodundid.core.terminal.terminalCommand.commands.apps;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppLoader;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.coreEvents.emcEvents.AppsReloadedEvent;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraftforge.common.MinecraftForge;

public class ReloadApps extends TerminalCommand {
	
	public ReloadApps() {
		super(CommandType.NORMAL);
		setCategory("App Specific");
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
				reload(termIn, runVisually);
			}
		}
	}
	
	private void reload(ETerminal termIn, boolean runVisually) {
		try {
			MinecraftForge.EVENT_BUS.post(new AppsReloadedEvent.Pre());
			
			termIn.writeln("Reloading all EMCApps!!\n");
			EArrayList<EMCApp> apps = new EArrayList(EnhancedMC.getApps().getAppsList());
			for (EMCApp app : apps) {
				AppLoader.reloadApp(termIn, app, true);
			}
			termIn.setInputEnabled(true);
			termIn.writeln("\nApps reloaded!");
			
			MinecraftForge.EVENT_BUS.post(new AppsReloadedEvent.Post());
		}
		catch (Exception e) { error(termIn, e); }
	}
	
}