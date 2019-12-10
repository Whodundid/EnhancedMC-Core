package com.Whodundid.core.debug.terminal.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.debug.terminal.TerminalCommandHandler;
import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.util.EnumChatFormatting;

public class Help implements IConsoleCommand {
	
	TerminalCommandHandler handler;
	
	@Override public String getName() { return "help"; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getCommandHelpInfo() { return "List all commands with aliases and can display info on a specific command."; }
	@Override public String getCommandErrorInfo(String arg) { return null; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		handler = EnhancedMC.getTerminal();
		if (args.size() == 0) {
			for (IConsoleCommand command : handler.getCommandList()) {
				if (command.getAliases() == null) {
					conIn.writeln("-" + command.getName(), 0xb2b2b2);
				} else {
					String a = EnumChatFormatting.GREEN + " ";
					//System.out.println(command.getName() + " " + command.getAliases().size());
					for (int i = 0; i < command.getAliases().size(); i++) {
						String commandAlias = command.getAliases().get(i);
						if (i == command.getAliases().size() - 1) { a += commandAlias; }
						else { a += (commandAlias + ", "); }
					}
					conIn.writeln("-" + command.getName() + ": " + a, 0xb2b2b2);
				}					
			}
			conIn.writeln("To see help information on a specific command, type help followed by the command.", 0xFF00DD);
		} else if (args.size() == 1) {
			String commandName = args.get(0);
			if (handler.getCommandNames().contains(commandName)) {
				IConsoleCommand command = handler.getCommand(commandName);
				conIn.writeln(command.getCommandHelpInfo(), 0xffff00);
			}
			else {
				conIn.error("Unrecognized command name");
			}
		}
	}
}
