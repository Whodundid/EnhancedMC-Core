package com.Whodundid.hotkeys.terminal;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.hotkeys.HotKeyApp;

public class CreateExampleKey extends TerminalCommand {
	
	public CreateExampleKey() {
		super(CommandType.APP_COMMAND);
		setCategory("Hotkeys");
		numArgs = 0;
	}
	
	@Override public String getName() { return "hkcreateexamplekey"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("hkcreateexample", "hkexample"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Creates the example hotkey"; }
	@Override public String getUsage() { return "ex: hkexample"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) { termIn.error("This command takes no arguments!"); }
		else {
			try {
				HotKeyApp app = (HotKeyApp) EnhancedMC.getApps().getApp(AppType.HOTKEYS);
				if (app != null) {
					app.createExampleKey();
					termIn.writeln("Creating ExampleHotKey..", 0x55ff55);
					EnhancedMC.reloadAllWindows();
				}
				else { termIn.error("Hotkeys is null!"); }
			}
			catch (Exception e) { error(termIn, e); }
		}
	}
	
}
