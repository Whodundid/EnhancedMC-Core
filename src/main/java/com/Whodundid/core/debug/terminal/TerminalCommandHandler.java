package com.Whodundid.core.debug.terminal;

import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.debug.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.debug.terminal.terminalCommand.commands.*;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.util.Iterator;
import java.util.List;

public class TerminalCommandHandler {

	public static final String version = "1.0";
	private static TerminalCommandHandler instance;
	protected StorageBoxHolder<String, IConsoleCommand> commands;
	protected EArrayList<IConsoleCommand> commandList;
	protected EArrayList<IConsoleCommand> customCommandList;
	
	public static TerminalCommandHandler getInstance() {
		return instance == null ? instance = new TerminalCommandHandler() : instance;
	}
	
	private TerminalCommandHandler() {
		commands = new StorageBoxHolder();
		commandList = new EArrayList();
		customCommandList = new EArrayList();
		registerBaseCommands(false);
	}
	
	
	private void registerBaseCommands(boolean runVisually) { registerBaseCommands(null, runVisually); }
	private void registerBaseCommands(ETerminal conIn, boolean runVisually) {
		registerCommand(conIn, new Help(), runVisually);
		registerCommand(conIn, new ReregisterCommands(), runVisually);
		registerCommand(conIn, new ClearTerminal(), runVisually);
		registerCommand(conIn, new ClearTerminalHistory(), runVisually);
		registerCommand(conIn, new ClearObjects(), runVisually);
		registerCommand(conIn, new DebugControl(), runVisually);
		registerCommand(conIn, new ListCMD(), runVisually);
		registerCommand(conIn, new Say(), runVisually);
		registerCommand(conIn, new Exit(), runVisually);
		registerCommand(conIn, new ModInfo(), runVisually);
		registerCommand(conIn, new Config(), runVisually);
		registerCommand(conIn, new Version(), runVisually);
		registerCommand(conIn, new EnableMod(), runVisually);
		registerCommand(conIn, new DisableMod(), runVisually);
		registerCommand(conIn, new ToggleSafeRM(), runVisually);
		registerCommand(conIn, new Server(), runVisually);
	}
	
	public void registerCommand(IConsoleCommand command, boolean runVisually) { registerCommand(null, command, runVisually); }
	public void registerCommand(ETerminal conIn, IConsoleCommand command, boolean runVisually) {
		commandList.add(command);
		commands.put(command.getName(), command);
		if (conIn != null & runVisually) { conIn.writeln("Registering command call: " + command.getName(), 0xffff00); }
		if (command.getAliases() != null) {
			for (int i = 0; i < command.getAliases().size(); i++) {
				commands.put(command.getAliases().get(i), command);
				if (conIn != null & runVisually) { conIn.writeln("Registering command alias: " + command.getAliases().get(i), 0x55ff55); }
			}
		}
	}
	
	public void executeCommand(ETerminal conIn, String cmd) {
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
					if (arg.equals("-i")) {
						runVisually = true;
						i.remove();
						break;
					}
				}
				
				command.runCommand(conIn, commandArguments, runVisually);
				
				if (!command.getName().equals("clear")) {
					conIn.writeln();
				}				
				return;
			}
		}
		conIn.writeln("Unrecognized command.", 0xff5555);
	}
	
	public synchronized void reregisterAllCommands(boolean runVisually) { reregisterAllCommands(null, runVisually); }
	public synchronized void reregisterAllCommands(ETerminal conIn, boolean runVisually) {
		Iterator<IConsoleCommand> a = commandList.iterator();
		while (a.hasNext()) {
			String commandName = a.next().getName();
			if (conIn != null & runVisually) { conIn.writeln("Unregistering command: " + commandName, 0xb2b2b2); }
			a.remove();
		}
		Iterator<StorageBox<String, IConsoleCommand>> b = commands.iterator();
		while (b.hasNext()) {
			String commandName = b.next().getObject();
			if (conIn != null & runVisually) { conIn.writeln("Unregistering command alias: " + commandName, 0xb2b2b2); }
		}
		registerBaseCommands(conIn, runVisually);
		customCommandList.forEach(c -> registerCommand(conIn, c, runVisually));
		
		//add way for sub mods to reregister their commands as well
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
