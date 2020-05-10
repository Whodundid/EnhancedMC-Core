package com.Whodundid.core.terminal.terminalCommand.commands.apps;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppSettings;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.settings.guiParts.ReloaderDialogueBox.Reason;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.util.EnumChatFormatting;

//Author: Hunter Bragg

public class Config extends TerminalCommand {
	
	public Config() {
		super(CommandType.NORMAL);
		numArgs = 2;
	}

	@Override public String getName() { return "config"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("cfig", "con"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Used to interface with EMC App config files. (reload, save, reset)"; }
	@Override public String getUsage() { return "ex: config core reload"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		EArrayList<String> options = new EArrayList("all");
		for (EMCApp m : RegisteredApps.getAppsList()) {
			if (m.getNameAliases().isNotEmpty()) {
				String name = m.getNameAliases().get(m.getNameAliases().size() - 1);
				options.add(name);
			}
		}
		
		if (args.size() <= 1) { super.basicTabComplete(termIn, args, options); }
		else if (args.size() == 2) { super.basicTabComplete(termIn, args, new EArrayList("reload", "load", "save", "reset")); }
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.info(getUsage()); }
		else if (args.size() > 2) { termIn.error("Too many arguments!"); }
		else {
			EMCApp mod = RegisteredApps.getAppByAlias(args.get(0));
			String input = args.get(0).toLowerCase();
			
			boolean all = input.equals("all") || input.equals("a");
			
			if (mod != null || all) {
				switch (args.get(1)) {
				case "rel":
				case "reload":
				case "l":
				case "load": loadConfig(termIn, args, runVisually, all ? null : mod); break;
				case "s":
				case "save": saveConfig(termIn, args, runVisually, all ? null : mod); break;
				case "res":
				case "reset": resetConfig(termIn, args, runVisually, all ? null : mod); break;
				default: termIn.error("Invalid action argument!");
				}
			}
			else { termIn.error("No App by that name found!"); }
		}
	}
	
	private void loadConfig(ETerminal termIn, EArrayList<String> args, boolean runVisually, EMCApp modIn) {
		if (modIn != null) {
			termIn.info("Reloading " + modIn.getName() + " config..");
			if (modIn.getConfig().loadAllConfigs()) { termIn.writeln("Success", EColors.green); }
			else { termIn.writeln("Failed to reload config!", EColors.lred); }
			for (WindowParent w : EnhancedMC.getAllActiveWindows()) { w.sendArgs("Reload", modIn); }
		}
		else {
			termIn.info("Reloading all EMC configs..\n");
			
			AppSettings.loadConfig();
			
			EArrayList<EMCApp> reloadedMods = new EArrayList();
			StorageBoxHolder<EMCApp, Reason> failedMods = new StorageBoxHolder();
			
			for (EMCApp m : RegisteredApps.getRegisteredAppList()) {
				boolean load = m.getConfig().loadAllConfigs();
				if (load) { reloadedMods.add(m); }
				else { failedMods.add(m, Reason.Loading); }
				for (WindowParent w : EnhancedMC.getAllActiveWindows()) { w.sendArgs("Reload", m); }
			}
			
			if (reloadedMods.isNotEmpty()) {
				termIn.writeln("Passed", EColors.green);
				for (EMCApp m : reloadedMods) {
					termIn.writeln("  -" + m.getName(), EColors.lgray);
				}
			}
			
			if (failedMods.isNotEmpty()) {
				termIn.writeln("\nFailed", EColors.lred);
				for (StorageBox<EMCApp, Reason> box : failedMods) {
					termIn.writeln(EnumChatFormatting.GRAY + "  -" + box.getObject().getName() + " : " + EnumChatFormatting.DARK_PURPLE + EnumChatFormatting.ITALIC + box.getValue().msg);
				}
			}
			
			
		}
	}
	
	private void saveConfig(ETerminal termIn, EArrayList<String> args, boolean runVisually, EMCApp modIn) {
		if (modIn != null) {
			termIn.info("Saving " + modIn.getName() + " config..");
			if (modIn.getConfig().saveAllConfigs()) { termIn.writeln("Success", EColors.green); }
			else { termIn.writeln("Failed to save config!", EColors.lred); }
		}
		else {
			termIn.info("Saving all EMC configs..\n");
			
			AppSettings.saveConfig();
			
			EArrayList<EMCApp> savedMods = new EArrayList();
			StorageBoxHolder<EMCApp, Reason> failedMods = new StorageBoxHolder();
			
			for (EMCApp m : RegisteredApps.getRegisteredAppList()) {
				boolean save = m.getConfig().saveAllConfigs();
				if (save) { savedMods.add(m); }
				else { failedMods.add(m, Reason.Saving); }
			}
			
			if (savedMods.isNotEmpty()) {
				termIn.writeln("Passed", EColors.green);
				for (EMCApp m : savedMods) {
					termIn.writeln("  -" + m.getName(), EColors.lgray);
				}
			}
			
			if (failedMods.isNotEmpty()) {
				termIn.writeln("\nFailed", EColors.lred);
				for (StorageBox<EMCApp, Reason> box : failedMods) {
					termIn.writeln(EnumChatFormatting.GRAY + "  -" + box.getObject().getName() + " : " + EnumChatFormatting.DARK_PURPLE + EnumChatFormatting.ITALIC + box.getValue().msg);
				}
			}
		}
	}
	
	private void resetConfig(ETerminal termIn, EArrayList<String> args, boolean runVisually, EMCApp modIn) {
		if (modIn != null) {
			termIn.info("Resetting " + modIn.getName() + " config..");
			if (modIn.getConfig().resetAllConfigs()) { termIn.writeln("Success", EColors.green); }
			else { termIn.writeln("Failed to reset config!", EColors.lred); }
			for (WindowParent w : EnhancedMC.getAllActiveWindows()) { w.sendArgs("Reload", modIn); }
		}
		else {
			termIn.info("Resetting all EMC configs..\n");
			
			AppSettings.resetConfig();
			
			EArrayList<EMCApp> resetMods = new EArrayList();
			StorageBoxHolder<EMCApp, Reason> failedMods = new StorageBoxHolder();
			
			for (EMCApp m : RegisteredApps.getRegisteredAppList()) {
				boolean reset = m.getConfig().resetAllConfigs();
				if (reset) { resetMods.add(m); }
				else { failedMods.add(m, Reason.Resetting); }
				for (WindowParent w : EnhancedMC.getAllActiveWindows()) { w.sendArgs("Reload", m); }
			}
			
			if (resetMods.isNotEmpty()) {
				termIn.writeln("Passed", EColors.green);
				for (EMCApp m : resetMods) {
					termIn.writeln("  -" + m.getName(), EColors.lgray);
				}
			}
			
			if (failedMods.isNotEmpty()) {
				termIn.writeln("\nFailed", EColors.lred);
				for (StorageBox<EMCApp, Reason> box : failedMods) {
					termIn.writeln(EnumChatFormatting.GRAY + "  -" + box.getObject().getName() + " : " + EnumChatFormatting.DARK_PURPLE + EnumChatFormatting.ITALIC + box.getValue().msg);
				}
			}
		}
	}
}
