package com.Whodundid.core.debug.console.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.debug.console.ConsoleCommandHandler;
import com.Whodundid.core.debug.console.gui.EConsole;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class Help implements IConsoleCommand {
	
	ConsoleCommandHandler handler = EnhancedMC.getConsole();
	
	@Override public String getName() { return "help"; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getCommandHelpInfo() { return "List all console commands and display help info on a specific command."; }
	@Override public String getCommandErrorInfo(String arg) { return null; }
	
	@Override
	public void runCommand(EConsole conIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() == 0) {
			for (IConsoleCommand command : handler.getCommandList()) {
				if (command.getAliases() == null) {
					//man.getConsole().writeln(command.getName(), Color.ORANGE);
				} else {
					//man.getConsole().write(command.getName() + ": ", Color.ORANGE);
					for (int i = 0; i < command.getAliases().size(); i++) {
						String commandAlias = command.getAliases().get(i);
						if (i == command.getAliases().size() - 1) {
							//man.getConsole().write(commandAlias, Color.CYAN);
						} else {
							//man.getConsole().write(commandAlias + ", ", Color.CYAN);
						}							
					}
					//man.getConsole().writeln();
				}					
			}
			//man.getConsole().writeln("To see help information on a specific command, type help followed by the command.", Color.WHITE);
		} else if (args.size() == 1) {
			String commandName = args.get(0);
			//if (man.getConsole().getConsoleCommandHandler().getCommandNames().contains(commandName)) {
			//	IConsoleCommand command = man.getConsole().getConsoleCommandHandler().getCommand(commandName);
			//	man.getConsole().writeln(command.getCommandHelpInfo(), Color.YELLOW);
			//}
		}
	}
}
