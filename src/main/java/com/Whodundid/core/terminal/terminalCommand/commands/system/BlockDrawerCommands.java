package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.renderUtil.BlockDrawer;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class BlockDrawerCommands extends TerminalCommand {

	public BlockDrawerCommands() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 1;
	}

	@Override public String getName() { return "blockdrawer"; }
	@Override public boolean showInHelp() { return EnhancedMC.isDevMode(); }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("bdrawer", "bdraw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Interface for the EMC Block Drawer API"; }
	@Override public String getUsage() { return "ex: bdraw clear"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		super.basicTabComplete(termIn, args, new EArrayList("enable", "disable", "clear"));
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { }
		else if (args.size() == 1) {
			String cmd = args.get(0);
			
			switch (cmd) {
			case "e":
			case "en":
			case "enable": enable(termIn); break;
			case "d":
			case "dis":
			case "disable": disable(termIn); break;
			case "c":
			case "clr":
			case "clear": clear(termIn); break;
			default: termIn.error("Invalid option!");
			}
		}
		else { termIn.error("Too Many Arguments!"); }
	}
	
	private void enable(ETerminal termIn) {
		if (CoreApp.enableBlockDrawer.get()) { termIn.info("BlockDrawer is already enabled."); }
		else {
			CoreApp.enableBlockDrawer.set(true);
			termIn.writeln("BlockDrawer enabled", EColors.green);
		}
	}
	
	private void disable(ETerminal termIn) {
		if (!CoreApp.enableBlockDrawer.get()) { termIn.info("BlockDrawer is already disabled."); }
		else {
			CoreApp.enableBlockDrawer.set(false);
			BlockDrawer.clearBlocks();
			termIn.writeln("BlockDrawer disabled", EColors.green);
		}
	}
	
	private void clear(ETerminal termIn) {
		BlockDrawer.clearBlocks();
		termIn.writeln("BlockDrawer cleared", EColors.green);
	}
	
}
