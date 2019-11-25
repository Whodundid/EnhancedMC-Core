package com.Whodundid.core.debug.console;

import com.Whodundid.core.debug.console.commands.IConsoleCommand;
import com.Whodundid.core.debug.console.gui.EConsole;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.util.Iterator;
import java.util.List;

public class ConsoleCommandHandler {

	private static ConsoleCommandHandler instance;
	protected StorageBoxHolder<String, IConsoleCommand> commands;
	protected EArrayList<IConsoleCommand> commandList;
	protected EArrayList<IConsoleCommand> customCommandList;
	
	public static ConsoleCommandHandler getInstance() {
		return instance == null ? instance = new ConsoleCommandHandler() : instance;
	}
	
	private ConsoleCommandHandler() {
		commands = new StorageBoxHolder();
		commandList = new EArrayList();
		customCommandList = new EArrayList();
		registerBaseCommands(false);
	}
	
	private void registerBaseCommands(boolean runVisually) {
		
	}
	
	public void registerCommand(IConsoleCommand command, boolean runVisually) {
		commandList.add(command);
		//if (runVisually) { man.getConsole().writeln("Registering command: " + command.getName(), Color.ORANGE); }
		commands.put(command.getName(), command);
		//if (runVisually) { man.getConsole().writeln("Registering command call: " + command.getName(), Color.ORANGE); }
		if (command.getAliases() != null) {
			for (int i = 0; i < command.getAliases().size(); i++) {
				commands.put(command.getAliases().get(i), command);
				//if (runVisually) { man.getConsole().writeln("Registering command alias: " + command.getAliases().get(i), Color.ORANGE); }
			}
		}
	}
	
	public void executeCommand(EConsole conIn, String cmd) {
		cmd = cmd.trim().toLowerCase();
		String[] commandParts = cmd.split(" ");
		EArrayList<String> commandArguments = new EArrayList();
		String baseCommand = "";
		if (commandParts.length > 0) {
			baseCommand = commandParts[0];
			for (int i = 1; i < commandParts.length; i++) {
				commandArguments.add(commandParts[i]);
			}
			if (commands.getBoxWithObj(baseCommand) != null) {
				IConsoleCommand command = commands.getBoxWithObj(baseCommand).getValue();
				
				if (command == null) {
					//man.getConsole().writeln("Unrecognized command.", Color.RED);
					return;
				}
				
				boolean runVisually = false;
				Iterator<String> i = commandArguments.iterator();
				while (i.hasNext()) {
					String arg = i.next();
					if (arg.equals("-v")) {
						runVisually = true;
						i.remove();
						break;
					}
				}
				
				command.runCommand(conIn, commandArguments, runVisually);
				
				if (!command.getName().equals("clear")) {
					//man.getConsole().writeln();
				}				
				return;
			}
		}
		//man.getConsole().writeln("Unrecognized command.", Color.RED);
	}
	
	public synchronized void reregisterAllCommands(boolean runVisually) {
		Iterator<IConsoleCommand> a = commandList.iterator();
		while (a.hasNext()) {
			String commandName = a.next().getName();
			//if (runVisually) { man.getConsole().writeln("Unregistering command: " + commandName, Color.ORANGE); }
			a.remove();
		}
		Iterator<StorageBox<String, IConsoleCommand>> b = commands.iterator();
		while (b.hasNext()) {
			String commandName = b.next().getObject();
			//if (runVisually) { man.getConsole().writeln("Unregistering command call: " + commandName, Color.ORANGE); }
		}
		registerBaseCommands(runVisually);
		for (IConsoleCommand command : customCommandList) {
			registerCommand(command, runVisually);
		}
	}
	
	public IConsoleCommand getCommand(String commandName) {
		StorageBox<String, IConsoleCommand> box = commands.getBoxWithObj(commandName);
		if (box != null) {
			return commands.getBoxWithObj(commandName).getValue();
		}
		//print doesn't exist in console
		return null;
	}
	
	public EArrayList<IConsoleCommand> getCommandList() { return commandList; }
	public List<String> getCommandNames() { return commands.getObjects(); }
}
