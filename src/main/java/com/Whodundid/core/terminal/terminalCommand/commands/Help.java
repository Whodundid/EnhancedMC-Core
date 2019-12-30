package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.TerminalCommandHandler;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.util.EnumChatFormatting;

public class Help implements IConsoleCommand {
	
	TerminalCommandHandler handler;
	
	@Override public String getName() { return "help"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("h"); }
	@Override public String getHelpInfo(boolean runVisually) { return "List all commands with aliases and can display info on a specific command."; }
	@Override public String getUsage() { return "ex: help deb"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		handler = EnhancedMC.getTerminalHandler();
		if (args.size() == 0) {
			conIn.writeln("Available commands:", EColors.cyan);
			for (IConsoleCommand command : handler.getCommandList()) {
				if (command.showInHelp()) {
					if (command.getAliases() == null) {
						conIn.writeln("-" + command.getName(), 0xb2b2b2);
					} else {
						String a = EnumChatFormatting.GREEN + "";
						for (int i = 0; i < command.getAliases().size(); i++) {
							String commandAlias = command.getAliases().get(i);
							if (i == command.getAliases().size() - 1) { a += commandAlias; }
							else { a += (commandAlias + ", "); }
						}
						conIn.writeln("-" + command.getName() + ": " + a, 0xb2b2b2);
					}
				}
			}
			conIn.writeln("");
			conIn.writeln("To see help information on a specific command, type help followed by the command.", EColors.yellow);
			conIn.writeln("To run a command with more information, add -i after the command. ex: list -i", EColors.yellow);
		} else if (args.size() == 1) {
			String commandName = args.get(0);
			if (handler.getCommandNames().contains(commandName)) {
				IConsoleCommand command = handler.getCommand(commandName);
				if (command.showInHelp()) {
					conIn.writeln(command.getHelpInfo(runVisually), 0xffff00);
					if (command.getUsage() != null && !command.getUsage().isEmpty()) {
						conIn.writeln(command.getUsage(), 0xffff00);
					}
				}
				else { conIn.error("Unrecognized command name"); }
			}
			else { conIn.error("Unrecognized command name"); }
		} else {
			conIn.error("Too many arguments!");
		}
	}
}
