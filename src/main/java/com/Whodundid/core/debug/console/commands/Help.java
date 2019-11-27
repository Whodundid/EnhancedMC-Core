package com.Whodundid.core.debug.console.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.debug.console.ConsoleCommandHandler;
import com.Whodundid.core.debug.console.gui.EConsole;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class Help implements IConsoleCommand {
	
	ConsoleCommandHandler handler;
	
	@Override public String getName() { return "help"; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getCommandHelpInfo() { return "List all console commands and display help info on a specific command."; }
	@Override public String getCommandErrorInfo(String arg) { return null; }
	
	@Override
	public void runCommand(EConsole conIn, EArrayList<String> args, boolean runVisually) {
		handler = EnhancedMC.getConsole();
		if (args.size() == 0) {
			for (IConsoleCommand command : handler.getCommandList()) {
				if (command.getAliases() == null) {
					conIn.writeln(command.getName(), 0xffaa00);
				} else {
					conIn.writeln(command.getName() + ": ", 0xffaa00);
					String a = "";
					for (int i = 0; i < command.getAliases().size(); i++) {
						String commandAlias = command.getAliases().get(i);
						if (i == command.getAliases().size() - 1) { a += commandAlias; }
						else { a += (commandAlias + ", "); }
					}
					conIn.writeln(a, 0x22ff88);
				}					
			}
			conIn.writeln("To see help information on a specific command, type help followed by the command.", 0xFF00DD);
		} else if (args.size() == 1) {
			String commandName = args.get(0);
			if (handler.getCommandNames().contains(commandName)) {
				IConsoleCommand command = handler.getCommand(commandName);
				conIn.writeln(command.getCommandHelpInfo(), 0xff5599);
			}
			else {
				conIn.writeln("Unrecognized command name", 0xff5555);
			}
		}
	}
}
