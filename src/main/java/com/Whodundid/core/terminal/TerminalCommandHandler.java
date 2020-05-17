package com.Whodundid.core.terminal;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.terminalCommand.commands.apps.*;
import com.Whodundid.core.terminal.terminalCommand.commands.fileSystem.*;
import com.Whodundid.core.terminal.terminalCommand.commands.system.*;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

//Author: Hunter Bragg

public class TerminalCommandHandler {

	public static final String version = "1.0";
	private static TerminalCommandHandler instance;
	protected StorageBoxHolder<String, TerminalCommand> commands;
	protected EArrayList<TerminalCommand> commandList;
	protected EArrayList<TerminalCommand> customCommandList;
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
	private void registerBaseCommands(ETerminal termIn, boolean runVisually) {
		//system
		registerCommand(new BlockDrawerCommands(), termIn, runVisually);
		registerCommand(new ClearObjects(), termIn, runVisually);
		registerCommand(new ClearTerminal(), termIn, runVisually);
		registerCommand(new ClearTerminalHistory(), termIn, runVisually);
		registerCommand(new CloseWindow(), termIn, runVisually);
		registerCommand(new DebugControl(), termIn, runVisually);
		registerCommand(new Exit(), termIn, runVisually);
		registerCommand(new Help(), termIn, runVisually);
		registerCommand(new ListCMD(), termIn, runVisually);
		registerCommand(new OpenGui(), termIn, runVisually);
		registerCommand(new ReregisterCommands(), termIn, runVisually);
		registerCommand(new Say(), termIn, runVisually);
		registerCommand(new Version(), termIn, runVisually);
		registerCommand(new WhoAmI(), termIn, runVisually);
		registerCommand(new OpControl(), termIn, EnhancedMC.isOpMode() && runVisually);
		registerCommand(new RuntimeCMD(), termIn, runVisually);
		registerCommand(new ViewTexture(), termIn, runVisually);
		registerCommand(new NotificationControl(), termIn, runVisually);
		if (EnhancedMC.isOpMode()) { registerCommand(new ForLoop(), termIn, runVisually); }
		if (EnhancedMC.isOpMode()) { registerCommand(new Server(), termIn, runVisually); }
		
		//apps
		registerCommand(new AppInfo(), termIn, runVisually);
		registerCommand(new Config(), termIn, runVisually);
		registerCommand(new DisableApp(), termIn, runVisually);
		registerCommand(new EnableApp(), termIn, runVisually);
		registerCommand(new ReloadApps(), termIn, runVisually);
		registerCommand(new ReloadApp(), termIn, runVisually);
		registerCommand(new ResetApp(), termIn, runVisually);
		
		//file system
		registerCommand(new Ls(), termIn, runVisually);
		registerCommand(new Cd(), termIn, runVisually);
		registerCommand(new Pwd(), termIn, runVisually);
		registerCommand(new Rm(), termIn, runVisually);
		registerCommand(new RmDir(), termIn, runVisually);
		registerCommand(new MkDir(), termIn, runVisually);
		registerCommand(new Mv(), termIn, runVisually);
		registerCommand(new Cp(), termIn, runVisually);
		registerCommand(new Lsblk(), termIn, runVisually);
		registerCommand(new Cat(), termIn, runVisually);
		registerCommand(new Edit(), termIn, runVisually);
		registerCommand(new Open(), termIn, runVisually);
	}
	
	private void registerSubModCommands(boolean runVisually) { registerSubModCommands(null, runVisually); }
	private void registerSubModCommands(ETerminal termIn, boolean runVisually) {
		for (EMCApp m : RegisteredApps.getAppsList()) {
			registerCommand(new EMCAppTerminalCommands(m), termIn, runVisually);
			m.terminalRegisterCommandEvent(termIn, runVisually);
		}
	}
	
	public void registerCommand(TerminalCommand command, boolean runVisually) { registerCommand(command, null, runVisually); }
	public void registerCommand(TerminalCommand command, ETerminal termIn, boolean runVisually) {
		commandList.add(command);
		commands.put(command.getName(), command);
		if (termIn != null & runVisually) { termIn.writeln("Registering command call: " + command.getName(), 0xffff00); }
		if (command.getAliases() != null) {
			for (int i = 0; i < command.getAliases().size(); i++) {
				commands.put(command.getAliases().get(i), command);
				if (termIn != null & runVisually) { termIn.writeln("Registering command alias: " + command.getAliases().get(i), 0x55ff55); }
			}
		}
	}
	
	public void executeCommand(ETerminal termIn, String cmd) { executeCommand(termIn, cmd, false, true); }
	public void executeCommand(ETerminal termIn, String cmd, boolean tab) { executeCommand(termIn, cmd, tab, true); }
	public void executeCommand(ETerminal termIn, String cmd, boolean tab, boolean addSpace) {
		boolean emptyEnd = cmd.endsWith(" ");
		
		cmd = cmd.trim().toLowerCase();
		String[] commandParts = cmd.split(" ");
		EArrayList<String> commandArguments = new EArrayList();
		String baseCommand = "";
		
		if (commandParts.length > 0) {
			baseCommand = commandParts[0];
			for (int i = 1; i < commandParts.length; i++) {
				commandArguments.add(commandParts[i]);
			}
			if (emptyEnd) { commandArguments.add(""); }
			
			if (commands.getBoxWithObj(baseCommand) != null) {
				TerminalCommand command = commands.getBoxWithObj(baseCommand).getValue();
				
				if (command == null) {
					termIn.error("Unrecognized command.");
					termIn.writeln();
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
				
				if (tab) {
					if (command.showInHelp()) { command.handleTabComplete(termIn, commandArguments); }
				}
				else {
					command.runCommand(termIn, commandArguments, runVisually);
				
					if (addSpace && (drawSpace && !command.getName().equals("clear"))) {
						termIn.writeln();
						drawSpace = true;
					}
				}
				
				return;
			}
		}
		termIn.writeln("Unrecognized command.\n", 0xff5555);
	}
	
	public synchronized void reregisterAllCommands(boolean runVisually) { reregisterAllCommands(null, runVisually); }
	public synchronized void reregisterAllCommands(ETerminal termIn, boolean runVisually) {
		Iterator<TerminalCommand> a = commandList.iterator();
		while (a.hasNext()) {
			String commandName = a.next().getName();
			if (termIn != null && runVisually) { termIn.writeln("Unregistering command: " + commandName, 0xb2b2b2); }
			a.remove();
		}
		
		Iterator<StorageBox<String, TerminalCommand>> b = commands.iterator();
		while (b.hasNext()) {
			String commandName = b.next().getObject();
			if (termIn != null && runVisually) { termIn.writeln("Unregistering command alias: " + commandName, 0xb2b2b2); }
			b.remove();
		}
		
		registerBaseCommands(termIn, runVisually);
		
		customCommandList.forEach(c -> registerCommand(c, termIn, runVisually));
		
		registerSubModCommands(termIn, runVisually);
	}
	
	public TerminalCommand getCommand(String commandName) {
		StorageBox<String, TerminalCommand> box = commands.getBoxWithObj(commandName);
		if (box != null) {
			return commands.getBoxWithObj(commandName).getValue();
		}
		return null;
	}
	
	public static EArrayList<String> getSortedCommandNames() {
		EArrayList<String> cmds = new EArrayList();
		StorageBoxHolder<CommandType, EArrayList<TerminalCommand>> sortedAll = getSortedCommands();
		
		for (StorageBox<CommandType, EArrayList<TerminalCommand>> box : sortedAll) {
			for (TerminalCommand command : box.getValue()) {
				if (EnhancedMC.isOpMode() || command.showInHelp()) {
					cmds.add(command.getName());
				}
			}
		}
		
		return cmds;
	}
	
	public static StorageBoxHolder<CommandType, EArrayList<TerminalCommand>> getSortedCommands() {
		StorageBoxHolder<CommandType, EArrayList<TerminalCommand>> commands = new StorageBoxHolder();
		
		//separate all commands into their own types
		for (TerminalCommand command : TerminalCommandHandler.getInstance().getCommandList()) {
			if (EnhancedMC.isOpMode() || command.showInHelp()) {
				CommandType type = command.getType();
				
				EArrayList<TerminalCommand> list = commands.getValueInBox(type);
				
				if (list != null) { list.add(command); }
				else { commands.add(type, new EArrayList(command)); }
				
			}
		}
		
		//alphabetically sort each
		for (StorageBox<CommandType, EArrayList<TerminalCommand>> box : commands) {
			EArrayList<TerminalCommand> list = box.getValue();
			
			if (list != null) { Collections.sort(list, new Sorter()); }
		}
		
		return commands;
	}
	
	private static class Sorter implements Comparator {

		@Override
		public int compare(Object a, Object b) {
			
			if (a instanceof TerminalCommand && b instanceof TerminalCommand) {
				String name1 = ((TerminalCommand) a).getName();
				String name2 = ((TerminalCommand) b).getName();
				
				return name1.compareToIgnoreCase(name2);
			}
			
			return 0;
		}
		
	}
	
	public EArrayList<TerminalCommand> getCommandList() { return commandList; }
	public List<String> getCommandNames() { return commands.getObjects(); }
	public EArrayList<String> getHistory() { return cmdHistory; }
	public TerminalCommandHandler clearHistory() { cmdHistory.clear(); return this; }
}
