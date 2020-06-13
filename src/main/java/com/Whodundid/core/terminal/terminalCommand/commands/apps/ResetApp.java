package com.Whodundid.core.terminal.terminalCommand.commands.apps;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class ResetApp extends TerminalCommand {
	
	private boolean responseReceived = false;
	private boolean confirmed = false;
	
	public ResetApp() {
		super(CommandType.NORMAL);
		setCategory("App Specific");
		numArgs = 1;
	}

	@Override public String getName() { return "reset"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Resets an EMC App back to its default settings"; }
	@Override public String getUsage() { return "ex: reset core"; }
	
	@Override
	public void handleTabComplete(ETerminal conIn, EArrayList<String> args) {
		EArrayList<String> options = new EArrayList("all");
		for (EMCApp a : RegisteredApps.getAppsList()) {
			for (String s : a.getNameAliases()) { options.add(s); }
		}
		
		super.basicTabComplete(conIn, args, options);
	}
	
	@Override
	public void onConfirmation(String response) {
		if (response != null) {
			responseReceived = true;
			confirmed = response.equals("y");
		}
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); termIn.info(getUsage()); }
		else if (args.size() == 1) {
			if (EnhancedMC.isInitialized()) {
				if (responseReceived) {
					if (confirmed) { reset(termIn, args, args.get(0).equals("all")); }
					else { termIn.info("User requested cancellation, reset aborted!\n"); }
					responseReceived = false;
					confirmed = false;
				}
				else { termIn.setRequiresCommandConfirmation(this, "Warning: You are about to reset potentially one or more EMC Apps. Do you want to continue? (Y, N)", args, runVisually); }
			}
		}
		else {  termIn.error("Too many arguments!"); termIn.info(getUsage()); }
	}
	
	private void reset(ETerminal termIn, EArrayList<String> args, boolean all) {
		if (all) {
			for (EMCApp app : RegisteredApps.getAppsList()) {
				app.getSettings().forEach(s -> s.set(s.getDefault()));
				if (app.isDisableable()) { app.setEnabled(false); }
				termIn.info(app.getName() + " reset!");
			}
			
			EnhancedMC.reloadAllWindows();
		}
		else {
			String theArg = args.get(0).toLowerCase();
			EMCApp app = RegisteredApps.getApp(theArg);
			
			if (app == null) { app = RegisteredApps.getAppByAlias(theArg); }
			
			if (app == null) { termIn.error("Cannot find an app by that input!"); }
			else {
				app.getSettings().forEach(s -> s.set(s.getDefault()));
				if (app.isDisableable()) { app.setEnabled(false); }
				termIn.info(app.getName() + " reset!");
				EnhancedMC.reloadAllWindows();
			}
		}
	}
	
}