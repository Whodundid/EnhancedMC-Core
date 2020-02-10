package com.Whodundid.core.terminal;

import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.ITerminalCommand;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.miscUtil.DataType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.ModSetting;

public class SubModTerminalCommands implements ITerminalCommand {
	
	private SubMod mod;
	
	public SubModTerminalCommands(SubMod modIn) {
		mod = modIn;
	}
	
	@Override public String getName() { return mod.getName().toLowerCase().replace(" ", ""); }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return mod.getNameAliases(); }
	@Override public String getHelpInfo(boolean runVisually) { return "Settings for " + mod.getName(); }
	@Override public String getUsage() { return "Type the mod name to see its list of available settings"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			if (mod.getSettings().isEmpty()) { conIn.writeln("No config settings available.", EColors.lgray); }
			else {
				conIn.info(getSettingsString());
			}
		}
		else if (args.size() < 2) { conIn.error("Not enough arguments!"); }
		else if (args.size() >= 2) {
			String settingName = args.get(0).toLowerCase();
			ModSetting setting = getSetting(settingName);
			
			if (setting != null) {
				if (setting.getType() == DataType.BOOL) { setSettingTF(setting, conIn, args); }
				if (setting.getType() == DataType.STRING) { setSettingString(setting, conIn, args); }
			}
			else { conIn.error("Unrecognized setting name!"); }
		}
	}
	
	private String getSettingsString() {
		String str = "";
		for (ModSetting s : mod.getSettings()) {
			str += s.getName() + ", ";
		}
		if (!str.isEmpty() && str.length() >= 2) { str = str.substring(0, str.length() - 2); }
		return str;
	}
	
	private ModSetting getSetting(String arg) {
		for (ModSetting s : RegisteredSubMods.getMod(mod.getName()).getSettings()) {
			if (s.getName().toLowerCase().equals(arg.toLowerCase())) { return s; }
		}
		return null;
	}
	
	private void setSettingTF(ModSetting<Boolean> settingIn, ETerminal conIn, EArrayList<String> args) {
		if (settingIn != null) {
			if (checkTF(args.get(1))) {
				boolean val = getTFValue(args.get(1));
				settingIn.set(val);
				conIn.writeln(args.get(0) + " set " + val, val ? EColors.green : EColors.lightRed);
			}
			else { conIn.error("Cannot parse argument: " + args.get(1)); }
		}
		else { conIn.badError("Setting is null!"); }
	}
	
	private void setSettingString(ModSetting<String> settingIn, ETerminal conIn, EArrayList<String> args) {
		if (settingIn != null) {
			
			String arg = args.get(1).toLowerCase();
			
			boolean contains = false;
			for (Object o : settingIn.getArgs()) {
				if (o.equals(arg)) { contains = true; }
			}
			
			if (contains) {
				settingIn.set(EUtil.capitalFirst(arg));
			}
			else { conIn.error("Invalid argument given - allowed chat args are: " + settingIn.getArgs()); return; }
			
			conIn.writeln(args.get(0) + " set " + EUtil.capitalFirst(args.get(1).toLowerCase()), EColors.green);
		}
		else { conIn.badError("Setting is null!"); }
	}
	
	private boolean checkTF(String argIn) {
		if (argIn != null) {
			String test = argIn.toLowerCase();
			switch (test) { case "t": case "true": case "f": case "false": return true; }
		}
		return false;
	}
	
	private boolean getTFValue(String argIn) {
		if (argIn != null) {
			String test = argIn.toLowerCase();
			switch (argIn) {
			case "t": case "true": return true;
			case "f": case "false": return false;
			}
		}
		return false;
	}
}
