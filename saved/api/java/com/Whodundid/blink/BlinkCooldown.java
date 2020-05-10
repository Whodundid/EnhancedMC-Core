package com.Whodundid.blink;

import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class BlinkCooldown extends TerminalCommand {
	
	public BlinkCooldown() {
		super(CommandType.MOD_COMMAND);
	}

	@Override public String getName() { return "blinkcooldown"; }
	@Override public boolean showInHelp() { return EnhancedMC.isOpMode(); }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("blinktime", "bcd"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Sets the cooldown for blink recharge"; }
	@Override public String getUsage() { return "ex: bcd 1000"; }
	@Override public void handleTabComplete(ETerminal conIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { conIn.info(getUsage()); }
		if (args.size() == 1) {
			String val = args.get(0);
			try {
				long time = Long.parseLong(val);
				BlinkApp mod = (BlinkApp) RegisteredApps.getApp(AppType.BLINK);
				mod.setBlinkCooldown(time);
				conIn.writeln("Setting recharge time to: " + time, EColors.green);
			}
			catch (Exception e) { conIn.error("Could not parse that value!"); }
		}
		else { conIn.error("Too many arguments!"); }
	}
}
