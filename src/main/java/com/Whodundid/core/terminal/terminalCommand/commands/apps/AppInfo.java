package com.Whodundid.core.terminal.terminalCommand.commands.apps;

import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.util.EnumChatFormatting;

//Author: Hunter Bragg

public class AppInfo extends TerminalCommand {
	
	public AppInfo() {
		super(CommandType.NORMAL);
		setCategory("App Specific");
		numArgs = 1;
	}

	@Override public String getName() { return "appinfo"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("info", "i"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays information on a specific EMC App"; }
	@Override public String getUsage() { return "ex: info core"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		EArrayList<String> options = new EArrayList();
		for (EMCApp a : RegisteredApps.getAppsList()) {
			for (String s : a.getNameAliases()) { options.add(s); }
		}
		
		super.basicTabComplete(termIn, args, options);
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() > 1) {
			termIn.error("Too many arguments!");
			termIn.error(getUsage());
		}
		else if (args.size() == 1) {
			EMCApp m = RegisteredApps.getAppByAlias(args.get(0));
			if (m == null) { termIn.error("No app by that name found!"); return; }
			
			termIn.writeln("App: " + m.getName(), EColors.cyan);
			termIn.writeln(m.isEnabled() ? "Enabled" : "Disabled", m.isEnabled() ? EColors.green : EColors.lred);
			termIn.writeln("Author: " + m.getAuthor(), EColors.lgray);
			termIn.writeln("Artist: " + m.getArtist(), EColors.lgray);
			termIn.writeln("Version: " + m.getVersion(), EColors.lgray);
			termIn.writeln("Version Date: " + m.getVersionDate(), EColors.lgray);
			if (m.getAdditionalInfo() != null) { termIn.writeln(m.getAdditionalInfo()); }
			
			if (m.getDependencies().isEmpty()) { termIn.writeln("Dependencies: none", EColors.lgray); }
			else {
				termIn.writeln("EMC Dependencies:", EColors.lgray);
				for (StorageBox<String, String> depBox : m.getDependencies()) {
					termIn.writeln(" -" + depBox.getObject() + " v" + depBox.getValue(), EColors.lgray);
				}
			}
			
			if (m.isIncompatible()) {
				termIn.writeln("Incompatible", EColors.lred);
				StorageBoxHolder<EMCApp, String> incompatMods = RegisteredApps.getAppImcompatibility(m);
				for (StorageBox<EMCApp, String> box : incompatMods) {
					EMCApp dep = box.getObject();
					termIn.writeln(" -" + EnumChatFormatting.RED + "requires " + EnumChatFormatting.YELLOW + 
								   dep.getName() + EnumChatFormatting.RED + " version '" + box.getValue() + "'", EColors.lgray);
				}
			}
		}
		else {
			termIn.info(getUsage());
		}
	}
	
}
