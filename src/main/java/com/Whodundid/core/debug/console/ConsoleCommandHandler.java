package com.Whodundid.core.debug.console;

import com.Whodundid.core.debug.console.commands.*;
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
		registerCommand(new Help(), runVisually);
		registerCommand(new ReregisterCommands(), runVisually);
		registerCommand(new ClearConsole(), runVisually);
		registerCommand(new ClearConsoleHistory(), runVisually);
		registerCommand(new DebugControl(), runVisually);
		registerCommand(new DebugCommandRunner(), runVisually);
	}
	
	public void registerCommand(IConsoleCommand command, boolean runVisually) { registerCommand(null, command, runVisually); }
	public void registerCommand(EConsole conIn, IConsoleCommand command, boolean runVisually) {
		commandList.add(command);
		if (conIn != null & runVisually) { conIn.writeln("Registering command: " + command.getName(), 0xffaa00); }
		commands.put(command.getName(), command);
		if (conIn != null & runVisually) { conIn.writeln("Registering command call: " + command.getName(), 0xffaa00); }
		if (command.getAliases() != null) {
			for (int i = 0; i < command.getAliases().size(); i++) {
				commands.put(command.getAliases().get(i), command);
				if (conIn != null & runVisually) { conIn.writeln("Registering command alias: " + command.getAliases().get(i), 0xffaa00); }
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
					conIn.writeln("Unrecognized command.", 0xff5555);
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
		conIn.writeln("Unrecognized command.", 0xff5555);
	}
	
	public synchronized void reregisterAllCommands(boolean runVisually) { reregisterAllCommands(null, runVisually); }
	public synchronized void reregisterAllCommands(EConsole conIn, boolean runVisually) {
		Iterator<IConsoleCommand> a = commandList.iterator();
		while (a.hasNext()) {
			String commandName = a.next().getName();
			if (conIn != null & runVisually) { conIn.writeln("Unregistering command: " + commandName, 0x00ffdc); }
			a.remove();
		}
		Iterator<StorageBox<String, IConsoleCommand>> b = commands.iterator();
		while (b.hasNext()) {
			String commandName = b.next().getObject();
			if (conIn != null & runVisually) { conIn.writeln("Unregistering command alias: " + commandName, 0x00ffdc); }
		}
		registerBaseCommands(runVisually);
		customCommandList.forEach(c -> registerCommand(c, runVisually));
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
