package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.TerminalCommandHandler;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.IListableCommand;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.util.EnumChatFormatting;

//Author: Hunter Bragg

public class Help extends TerminalCommand implements IListableCommand {
	
	public Help() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}
	
	@Override public String getName() { return "help"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("h", "commands", "cmds", "cmd"); }
	@Override public String getHelpInfo(boolean runVisually) { return "List all commands with aliases and can display info on a specific command."; }
	@Override public String getUsage() { return "ex: help deb"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		EArrayList<String> options = TerminalCommandHandler.getInstance().getSortedCommandNames();
		super.basicTabComplete(termIn, args, options);
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() == 0) {
			termIn.info("Help: (Command Name): followed by aliases\n");
			termIn.writeln("Available commands:", EColors.cyan);
			StorageBoxHolder<CommandType, EArrayList<TerminalCommand>> commands = TerminalCommandHandler.getInstance().getSortedCommands();
			
			for (StorageBox<CommandType, EArrayList<TerminalCommand>> box : commands) {
				if (box.getObject() == CommandType.MOD) { termIn.writeln(); termIn.writeln("EMC App Config Settings:", EColors.cyan); }
				if (box.getObject() == CommandType.MOD_COMMAND) { termIn.writeln(); termIn.writeln("EMC App Terminal Commands:", EColors.cyan); }
				
				for (TerminalCommand command : box.getValue()) {
					if (command.getAliases() == null) {
						termIn.writeln("  -" + command.getName(), 0xb2b2b2);
					}
					else {
						String a = EnumChatFormatting.GREEN + "";
						for (int i = 0; i < command.getAliases().size(); i++) {
							String commandAlias = command.getAliases().get(i);
							if (i == command.getAliases().size() - 1) { a += commandAlias; }
							else { a += (commandAlias + ", "); }
						}
						termIn.writeln("  -" + command.getName() + ": " + a, 0xb2b2b2);
					}
				}
			}
			termIn.writeln();
			termIn.writeln("To see help information on a specific command, type help followed by the command.", EColors.yellow);
			termIn.writeln("To run a command with more information, add -i after the command. ex: list -i", EColors.yellow);
			
		}
		else if (args.size() == 1) {
			
			String commandName = args.get(0);
			if (TerminalCommandHandler.getInstance().getCommandNames().contains(commandName)) {
				TerminalCommand command = TerminalCommandHandler.getInstance().getCommand(commandName);
				if (EnhancedMC.isOpMode() || command.showInHelp()) {
					termIn.writeln(command.getHelpInfo(runVisually), 0xffff00);
					if (command.getUsage() != null && !command.getUsage().isEmpty()) {
						termIn.writeln(command.getUsage(), 0xffff00);
					}
				}
				else { termIn.error("Unrecognized command name"); }
			}
			else { termIn.error("Unrecognized command name"); }
		}
		else { termIn.error("Too many arguments!"); }
	}

	@Override
	public void list(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.info("Listing all terminal commands\n");
		termIn.writeln("Available commands:", EColors.cyan);
		StorageBoxHolder<CommandType, EArrayList<TerminalCommand>> commands = TerminalCommandHandler.getInstance().getSortedCommands();
		
		for (StorageBox<CommandType, EArrayList<TerminalCommand>> box : commands) {
			if (box.getObject() == CommandType.MOD) { termIn.writeln(); termIn.writeln("EMC Mod Config Settings:", EColors.cyan); }
			if (box.getObject() == CommandType.MOD_COMMAND) { termIn.writeln(); termIn.writeln("EMC Mod Commands:", EColors.cyan); }
			
			for (TerminalCommand command : box.getValue()) {
				if (command.getAliases() == null) {
					termIn.writeln("  -" + command.getName(), 0xb2b2b2);
				} else {
					String a = EnumChatFormatting.GREEN + "";
					for (int i = 0; i < command.getAliases().size(); i++) {
						String commandAlias = command.getAliases().get(i);
						if (i == command.getAliases().size() - 1) { a += commandAlias; }
						else { a += (commandAlias + ", "); }
					}
					termIn.writeln("  -" + command.getName() + ": " + a, 0xb2b2b2);
				}
			}
		}
	}
	
}
