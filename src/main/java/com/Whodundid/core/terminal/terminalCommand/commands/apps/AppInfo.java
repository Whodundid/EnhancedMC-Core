package com.Whodundid.core.terminal.terminalCommand.commands.apps;

import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.util.EnumChatFormatting;

//Author: Hunter Bragg

public class AppInfo extends TerminalCommand {
	
	public AppInfo() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}

	@Override public String getName() { return "appinfo"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("info", "i"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays information on a specific EMC App"; }
	@Override public String getUsage() { return "ex: info core"; }
	
	@Override
	public void handleTabComplete(ETerminal conIn, EArrayList<String> args) {
		EArrayList<String> options = new EArrayList();
		for (EMCApp a : RegisteredApps.getAppsList()) {
			for (String s : a.getNameAliases()) { options.add(s); }
		}
		
		super.basicTabComplete(conIn, args, options);
	}
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() > 1) {
			conIn.error("Too many arguments!");
			conIn.error(getUsage());
		}
		else if (args.size() == 1) {
			EMCApp m = RegisteredApps.getAppByAlias(args.get(0));
			if (m == null) { conIn.error("No app by that name found!"); return; }
			
			conIn.writeln("App: " + m.getName(), EColors.cyan);
			conIn.writeln(m.isEnabled() ? "Enabled" : "Disabled", m.isEnabled() ? EColors.green : EColors.lred);
			conIn.writeln("Author: " + m.getAuthor(), EColors.lgray);
			conIn.writeln("Version: " + m.getVersion(), EColors.lgray);
			conIn.writeln("Version Date: " + m.getVersionDate(), EColors.lgray);
			
			if (m.getDependencies().isEmpty()) { conIn.writeln("Dependencies: none", EColors.lgray); }
			else {
				conIn.writeln("EMC Dependencies:", EColors.lgray);
				for (StorageBox<String, String> depBox : m.getDependencies()) {
					conIn.writeln(" -" + depBox.getObject() + " v" + depBox.getValue(), EColors.lgray);
				}
			}
			
			if (m.isIncompatible()) {
				conIn.writeln("Incompatible", EColors.lred);
				StorageBoxHolder<EMCApp, String> incompatMods = RegisteredApps.getAppImcompatibility(m);
				for (StorageBox<EMCApp, String> box : incompatMods) {
					EMCApp dep = box.getObject();
					conIn.writeln(" -" + EnumChatFormatting.RED + "requires " + EnumChatFormatting.YELLOW + 
								  dep.getName() + EnumChatFormatting.RED + " version '" + box.getValue() + "'", EColors.lgray);
				}
			}
		}
		else {
			conIn.info(getUsage());
		}
	}
}
