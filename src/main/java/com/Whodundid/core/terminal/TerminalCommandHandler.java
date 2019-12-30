package com.Whodundid.core.terminal;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.terminal.terminalCommand.commands.*;
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
	public static boolean drawSpace = true;
	public static EArrayList<String> cmdHistory = new EArrayList();
	
	public static TerminalCommandHandler getInstance() {
		return instance == null ? instance = new TerminalCommandHandler() : instance;
	}
	
	private TerminalCommandHandler() {
		commands = new StorageBoxHolder();
		commandList = new EArrayList();
		customCommandList = new EArrayList();
	}
	
	public void initCommands() {
		registerBaseCommands(false);
		registerSubModCommands(false);
	}
	
	private void registerBaseCommands(boolean runVisually) { registerBaseCommands(null, runVisually); }
	private void registerBaseCommands(ETerminal conIn, boolean runVisually) {
		
		registerCommand(new ClearObjects(), conIn, runVisually);
		registerCommand(new ClearTerminal(), conIn, runVisually);
		registerCommand(new ClearTerminalHistory(), conIn, runVisually);
		registerCommand(new Config(), conIn, runVisually);
		registerCommand(new DebugControl(), conIn, runVisually);
		registerCommand(new DisableMod(), conIn, runVisually);
		registerCommand(new EnableMod(), conIn, runVisually);
		registerCommand(new Exit(), conIn, runVisually);
		if (EnhancedMC.isOpMode()) { registerCommand(new ForLoop(), conIn, runVisually); }
		registerCommand(new Help(), conIn, runVisually);
		registerCommand(new ListCMD(), conIn, runVisually);
		registerCommand(new ModInfo(), conIn, runVisually);
		registerCommand(new OpenGui(), conIn, runVisually);
		registerCommand(new ReregisterCommands(), conIn, runVisually);
		//registerCommand(conIn, new ResetMod(), runVisually);
		registerCommand(new Say(), conIn, runVisually);
		if (EnhancedMC.isOpMode()) { registerCommand(new Server(), conIn, runVisually); }
		if (EnhancedMC.isOpMode()) { registerCommand(new ToggleSafeRM(), conIn, runVisually); }
		registerCommand(new Version(), conIn, runVisually);
		
		registerCommand(new OpControl(), conIn, false);
	}
	
	private void registerSubModCommands(boolean runVisually) { registerSubModCommands(null, runVisually); }
	private void registerSubModCommands(ETerminal conIn, boolean runVisually) {
		for (SubMod m : RegisteredSubMods.getModsList()) { m.eventTerminalRegister(conIn, runVisually); }
	}
	
	public void registerCommand(IConsoleCommand command, boolean runVisually) { registerCommand(command, null, runVisually); }
	public void registerCommand(IConsoleCommand command, ETerminal conIn, boolean runVisually) {
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
	
	public void executeCommand(ETerminal conIn, String cmd) { executeCommand(conIn, cmd, true); }
	public void executeCommand(ETerminal conIn, String cmd, boolean addSpace) {
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
					conIn.error("Unrecognized command.");
					conIn.writeln();
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
				
				if (addSpace && (drawSpace && !command.getName().equals("clear"))) {
					conIn.writeln();
					drawSpace = true;
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
			b.remove();
		}
		
		registerBaseCommands(conIn, runVisually);
		
		customCommandList.forEach(c -> registerCommand(c, conIn, runVisually));
		
		registerSubModCommands(conIn, runVisually);
	}
	
	public IConsoleCommand getCommand(String commandName) {
		StorageBox<String, IConsoleCommand> box = commands.getBoxWithObj(commandName);
		if (box != null) {
			return commands.getBoxWithObj(commandName).getValue();
		}
		return null;
	}
	
	public EArrayList<IConsoleCommand> getCommandList() { return commandList; }
	public List<String> getCommandNames() { return commands.getObjects(); }
	public EArrayList<String> getHistory() { return cmdHistory; }
	public TerminalCommandHandler clearHistory() { cmdHistory.clear(); return this; }
}
