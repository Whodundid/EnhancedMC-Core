package com.Whodundid.core.terminal;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.app.config.AppConfigSetting;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.miscUtil.DataType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.util.EnumChatFormatting;

public class EMCAppTerminalCommands extends TerminalCommand {
	
	private EMCApp app;
	
	public EMCAppTerminalCommands(EMCApp appIn) {
		super(CommandType.APP);
		app = appIn;
	}
	
	@Override public String getName() { return app.getName().toLowerCase().replace(" ", ""); }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return app.getNameAliases(); }
	@Override public String getHelpInfo(boolean runVisually) { return "Settings for " + app.getName(); }
	@Override public String getUsage() { return "Type the app name to see its list of available settings"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		if (args.size() == 0) {
			termIn.buildTabCompletions(app.getSettings().stream().filter(c -> !c.getRequiresDev() || EnhancedMC.isDevMode()).map(c -> c.getName()).collect(EArrayList.toEArrayList()));
		}
		else if (args.size() == 1) {
			if (!termIn.getTab1()) {
				int arg = termIn.getCurrentArg() - 1;
				String input = args.get(arg);
				
				EArrayList<String> options = new EArrayList();
				
				for (AppConfigSetting c : app.getSettings()) {
					if (!c.getRequiresDev() || EnhancedMC.isDevMode()) {
						if (c.getName().toLowerCase().startsWith(input)) { options.add(c.getName()); }
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
					
					if (!setting.getRequiresDev() || EnhancedMC.isDevMode()) {
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
			if (app.getSettings().isEmpty()) { termIn.writeln("No config settings available.", EColors.lgray); }
			else {
				EArrayList<AppConfigSetting> settings = new EArrayList();
				
				for (AppConfigSetting s : app.getSettings()) {
					if (!s.getRequiresDev()) { settings.add(s); }
					else if (EnhancedMC.isDevMode()) { settings.add(s); }
				}
				
				for (AppConfigSetting s : settings) {
					EnumChatFormatting color = EnumChatFormatting.WHITE;
					
					switch (s.getType()) {
					case BOOL: color = (boolean) s.get() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED; break;
					case BYTE:
					case SHORT:
					case INT:
					case LONG:
					case FLOAT:
					case DOUBLE: color = EnumChatFormatting.GOLD; break;
					case CHAR: 
					case STRING: color = EnumChatFormatting.AQUA; break;
					case OBJECT:
					default: color = EnumChatFormatting.LIGHT_PURPLE;
					}
					
					termIn.writeln(s.getName() + ": " + color + s.get(), EColors.yellow);
				}
			}
		}
		else if (args.size() < 1) { termIn.error("Not enough arguments!"); }
		else if (args.size() >= 1) {
			String settingName = args.get(0).toLowerCase();
			AppConfigSetting setting = getSetting(settingName);
			
			if (setting != null) {
				if (setting.getRequiresDev() && !EnhancedMC.isDevMode()) { termIn.error("Unrecognized setting name!"); }
				else {
					if (args.size() > 1) {
						switch (setting.getType()) {
						case BOOL: setSettingTF(setting, termIn, args); break;
						case BYTE:
						case SHORT:
						case INT:
						case LONG:
						case FLOAT:
						case DOUBLE: setSettingNumber(setting, termIn, args); break;
						case CHAR: 
						case STRING: setSettingString(setting, termIn, args); break;
						case OBJECT:
						default: termIn.error("Cannot set this value through the terminal!");
						}
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
		for (AppConfigSetting s : RegisteredApps.getApp(app.getName()).getSettings()) {
			if (s.getName().toLowerCase().equals(arg.toLowerCase())) { return s; }
		}
		return null;
	}
	
	private void setSettingTF(AppConfigSetting<Boolean> settingIn, ETerminal termIn, EArrayList<String> args) {
		if (settingIn != null) {
			if (checkTF(args.get(1))) {
				boolean val = getTFValue(args.get(1));
				settingIn.set(val);
				
				EMCApp app = settingIn.getApp();
				if (app != null && app.getConfig() != null) {
					app.getConfig().saveMainConfig();
				}
				
				EnumChatFormatting color = val ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
				termIn.writeln(settingIn.getDescription() + ": " + color + settingIn.get(), EColors.yellow);
				
				EnhancedMC.reloadAllWindows();
			}
			else { termIn.error("Cannot parse argument: " + args.get(1)); }
		}
		else {
			termIn.error("Setting is null!");
		}
	}
	
	private void setSettingString(AppConfigSetting<String> settingIn, ETerminal termIn, EArrayList<String> args) {
		if (settingIn != null) {
			
			String arg = args.get(1);
			
			boolean contains = false;
			for (Object o : settingIn.getArgs()) {
				if (o.equals(arg)) { contains = true; }
			}
			
			if (contains) {
				settingIn.set(arg);
				
				EMCApp app = settingIn.getApp();
				if (app != null && app.getConfig() != null) {
					app.getConfig().saveMainConfig();
				}
				
				EnhancedMC.reloadAllWindows();
			}
			else { termIn.error("Invalid argument given - allowed args are: " + settingIn.getArgs()); return; }
			
			termIn.writeln(settingIn.getDescription() + ": " + EnumChatFormatting.GREEN + settingIn.get(), EColors.yellow);
		}
		else { termIn.error("Setting is null!"); }
	}
	
	private void setSettingNumber(AppConfigSetting settingIn, ETerminal termIn, EArrayList<String> args) {
		if (settingIn != null) {
			String arg = args.get(1).toLowerCase();
			
			try {
				Number val = 0;
				
				switch (settingIn.getType()) {
				case BYTE: val = Byte.parseByte(arg); settingIn.set(val.byteValue()); break;
				case SHORT: val = Short.parseShort(arg); settingIn.set(val.shortValue()); break;
				case INT: val = Integer.parseInt(arg); settingIn.set(val.intValue()); break;
				case LONG: val = Long.parseLong(arg); settingIn.set(val.longValue()); break;
				case FLOAT: val = Float.parseFloat(arg); settingIn.set(val.floatValue()); break;
				case DOUBLE: val = Double.parseDouble(arg); settingIn.set(val.doubleValue()); break;
				default: termIn.error("Invalid nubmer type!"); break;
				}
				
				EMCApp app = settingIn.getApp();
				if (app != null && app.getConfig() != null) {
					app.getConfig().saveMainConfig();
				}
				
				termIn.writeln(settingIn.getDescription() + ": " + EnumChatFormatting.GREEN + settingIn.get(), EColors.yellow);
				
				EnhancedMC.reloadAllWindows();
			}
			catch (NumberFormatException e) {
				long val = Long.parseLong(arg.substring(2), 16);
				
				settingIn.set((int) val);
				
				EMCApp app = settingIn.getApp();
				if (app != null && app.getConfig() != null) {
					app.getConfig().saveMainConfig();
				}
				
				termIn.writeln(settingIn.getDescription() + ": " + EnumChatFormatting.GREEN + settingIn.get(), EColors.yellow);
			}
			catch (Exception e) {
				termIn.error("Failed to parse input!");
				error(termIn, e);
			}
		}
		else { termIn.error("Setting is null!"); }
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
