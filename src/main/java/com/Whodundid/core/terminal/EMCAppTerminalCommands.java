package com.Whodundid.core.terminal;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppConfigSetting;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.miscUtil.DataType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.util.EnumChatFormatting;

public class EMCAppTerminalCommands extends TerminalCommand {
	
	private EMCApp mod;
	
	public EMCAppTerminalCommands(EMCApp modIn) {
		super(CommandType.MOD);
		mod = modIn;
	}
	
	@Override public String getName() { return mod.getName().toLowerCase().replace(" ", ""); }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return mod.getNameAliases(); }
	@Override public String getHelpInfo(boolean runVisually) { return "Settings for " + mod.getName(); }
	@Override public String getUsage() { return "Type the mod name to see its list of available settings"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		if (args.size() == 0) {
			termIn.buildTabCompletions(mod.getSettings().stream().filter(c -> !c.getRequiresOp() || EnhancedMC.isOpMode()).map(c -> c.getName()).collect(EArrayList.toEArrayList()));
		}
		else if (args.size() == 1) {
			if (!termIn.getTab1()) {
				int arg = termIn.getCurrentArg() - 1;
				String input = args.get(arg);
				
				EArrayList<String> options = new EArrayList();
				
				for (AppConfigSetting c : mod.getSettings()) {
					if (!c.getRequiresOp() || EnhancedMC.isOpMode()) {
						if (c.getName().startsWith(input)) { options.add(c.getName()); }
					}
				}
				
				termIn.buildTabCompletions(options);
			}
		}
		else if (args.size() == 2) {
			if (!termIn.getTab1()) {
				AppConfigSetting setting = getSetting(args.get(termIn.getCurrentArg() - 2));
				String input = args.get(termIn.getCurrentArg() - 1);

				if (setting != null) {
					EArrayList<String> options = new EArrayList();
					
					if (!setting.getRequiresOp() || EnhancedMC.isOpMode()) {
						if (setting.getType() == DataType.BOOL) {
							options.add("true", "false");
						}
						else if (setting.getType() == DataType.STRING) {
							options.addAll(setting.getArgs());
						}
					}
					
					termIn.buildTabCompletions(options);
				}
			}
		}
		
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			if (mod.getSettings().isEmpty()) { termIn.writeln("No config settings available.", EColors.lgray); }
			else {
				EArrayList<AppConfigSetting> settings = new EArrayList();
				
				for (AppConfigSetting s : mod.getSettings()) {
					if (!s.getRequiresOp()) { settings.add(s); }
					else if (EnhancedMC.isOpMode()) { settings.add(s); }
				}
				
				for (AppConfigSetting s : settings) {
					EnumChatFormatting color = EnumChatFormatting.WHITE;
					System.out.println(s.getType());
					if (s.getType() == DataType.BOOL) { color = (boolean) s.get() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED; }
					if (s.getType() == DataType.INT) { color = EnumChatFormatting.GOLD; }
					if (s.getType() == DataType.STRING) { color = EnumChatFormatting.AQUA; }
					if (s.getType() == null) { color = EnumChatFormatting.LIGHT_PURPLE; }
					
					termIn.writeln(s.getName() + ": " + color + s.get(), EColors.yellow);
				}
			}
		}
		else if (args.size() < 1) { termIn.error("Not enough arguments!"); }
		else if (args.size() >= 1) {
			String settingName = args.get(0).toLowerCase();
			AppConfigSetting setting = getSetting(settingName);
			
			if (setting != null) {
				if (setting.getRequiresOp() && !EnhancedMC.isOpMode()) { termIn.error("Unrecognized setting name!"); }
				else {
					if (args.size() > 1) {
						if (setting.getType() == DataType.BOOL) { setSettingTF(setting, termIn, args); }
						if (setting.getType() == DataType.STRING) { setSettingString(setting, termIn, args); }
						if (setting.getType() == DataType.INT) { setSettingInt(setting, termIn, args); }
					}
					else {
						EnumChatFormatting color = EnumChatFormatting.WHITE;
						if (setting.getType() == DataType.BOOL) { color = ((Boolean) setting.get()) ? EnumChatFormatting.GREEN : EnumChatFormatting.RED; }
						
						termIn.writeln(setting.getDescription() + ": " + color + setting.get(), EColors.yellow);
					}
				}
			}
			else { termIn.error("Unrecognized setting name!"); }
		}
	}
	
	//---------------------------------------
	//EMCAppTerminalCommands Internal Methods
	//---------------------------------------
	
	private AppConfigSetting getSetting(String arg) {
		for (AppConfigSetting s : RegisteredApps.getApp(mod.getName()).getSettings()) {
			if (s.getName().toLowerCase().equals(arg.toLowerCase())) { return s; }
		}
		return null;
	}
	
	private void setSettingTF(AppConfigSetting<Boolean> settingIn, ETerminal termIn, EArrayList<String> args) {
		if (settingIn != null) {
			if (checkTF(args.get(1))) {
				boolean val = getTFValue(args.get(1));
				settingIn.set(val);
				
				EMCApp app = settingIn.getMod();
				if (app != null && app.getConfig() != null) {
					app.getConfig().saveMainConfig();
				}
				
				EnumChatFormatting color = val ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
				termIn.writeln(settingIn.getDescription() + ": " + color + val, EColors.yellow);
			}
			else { termIn.error("Cannot parse argument: " + args.get(1)); }
		}
		else { termIn.badError("Setting is null!"); }
	}
	
	private void setSettingString(AppConfigSetting<String> settingIn, ETerminal termIn, EArrayList<String> args) {
		if (settingIn != null) {
			
			String arg = args.get(1).toLowerCase();
			
			boolean contains = false;
			for (Object o : settingIn.getArgs()) {
				if (o.equals(arg)) { contains = true; }
			}
			
			if (contains) {
				settingIn.set(EUtil.capitalFirst(arg));
				
				EMCApp app = settingIn.getMod();
				if (app != null && app.getConfig() != null) {
					app.getConfig().saveMainConfig();
				}
			}
			else { termIn.error("Invalid argument given - allowed args are: " + settingIn.getArgs()); return; }
			
			termIn.writeln(settingIn.getDescription() + ": " + EnumChatFormatting.WHITE + EUtil.capitalFirst(args.get(1).toLowerCase()), EColors.yellow);
		}
		else { termIn.badError("Setting is null!"); }
	}
	
	private void setSettingInt(AppConfigSetting<Integer> settingIn, ETerminal termIn, EArrayList<String> args) {
		if (settingIn != null) {
			String arg = args.get(1).toLowerCase();
			
			try {
				int val = Integer.parseInt(arg);
				
				settingIn.set(val);
				
				EMCApp app = settingIn.getMod();
				if (app != null && app.getConfig() != null) {
					app.getConfig().saveMainConfig();
				}
				
				termIn.writeln(settingIn.getDescription() + ": " + EnumChatFormatting.WHITE + val, EColors.yellow);
			}
			catch (Exception e) {
				termIn.badError("Failed to parse input!");
				e.printStackTrace();
			}
		}
		else { termIn.badError("Setting is null!"); }
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
