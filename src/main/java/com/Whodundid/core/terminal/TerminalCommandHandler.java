package com.Whodundid.core.terminal;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.terminalCommand.commands.apps.*;
import com.Whodundid.core.terminal.terminalCommand.commands.fileSystem.*;
import com.Whodundid.core.terminal.terminalCommand.commands.hypixel.*;
import com.Whodundid.core.terminal.terminalCommand.commands.system.*;
import com.Whodundid.core.terminal.terminalCommand.commands.windows.Close;
import com.Whodundid.core.terminal.terminalCommand.commands.windows.MinimizeWindow;
import com.Whodundid.core.terminal.terminalCommand.commands.windows.PinWindow;
import com.Whodundid.core.terminal.terminalCommand.commands.windows.ShowWindow;
import com.Whodundid.core.terminal.terminalCommand.commands.windows.ToFrontWindow;
import com.Whodundid.core.terminal.window.ETerminal;
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
		
		//hypxiel
		registerCommand(new HypixelDataCMD(), termIn, runVisually);
		registerCommand(new UpdateHypixelData(), termIn, runVisually);
		
		//system
		registerCommand(new ClearObjects(), termIn, runVisually);
		registerCommand(new ClearTerminal(), termIn, runVisually);
		registerCommand(new ClearTerminalHistory(), termIn, runVisually);
		registerCommand(new Close(), termIn, runVisually);
		registerCommand(new DebugControl(), termIn, runVisually);
		registerCommand(new Help(), termIn, runVisually);
		registerCommand(new ListCMD(), termIn, runVisually);
		registerCommand(new OpenGui(), termIn, runVisually);
		registerCommand(new ReregisterCommands(), termIn, runVisually);
		registerCommand(new Say(), termIn, runVisually);
		registerCommand(new Version(), termIn, runVisually);
		registerCommand(new WhoAmI(), termIn, runVisually);
		registerCommand(new RuntimeCMD(), termIn, runVisually);
		registerCommand(new NotificationControl(), termIn, runVisually);
		registerCommand(new SystemCMD(), termIn, runVisually);
		registerCommand(new SetChat(), termIn, runVisually);
		registerCommand(new DevControl(), termIn, EnhancedMC.isDevMode() && runVisually);
		registerCommand(new ParseCode(), termIn, EnhancedMC.isDevMode() && runVisually);
		registerCommand(new ParseCode2(), termIn, EnhancedMC.isDevMode() && runVisually);
		if (EnhancedMC.isDevMode()) { registerCommand(new BlockDrawerCommands(), termIn, runVisually); }
		if (EnhancedMC.isDevMode()) { registerCommand(new ReloadResources(), termIn, EnhancedMC.isDevMode() && runVisually); }
		if (EnhancedMC.isDevMode()) { registerCommand(new ForLoop(), termIn, runVisually); }
		if (EnhancedMC.isDevMode()) { registerCommand(new Server(), termIn, runVisually); }
		if (EnhancedMC.isDevMode()) { registerCommand(new ViewTexture(), termIn, runVisually); }
		
		//windows
		registerCommand(new PinWindow(), termIn, runVisually);
		registerCommand(new ToFrontWindow(), termIn, runVisually);
		registerCommand(new ShowWindow(), termIn, runVisually);
		registerCommand(new MinimizeWindow(), termIn, runVisually);
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
		
		cmd = cmd.trim();
		String[] commandParts = cmd.split(" ");
		EArrayList<String> commandArguments = new EArrayList();
		String baseCommand = "";
		
		if (commandParts.length > 0) {
			baseCommand = commandParts[0].toLowerCase();
			for (int i = 1; i < commandParts.length; i++) {
				commandArguments.add(commandParts[i]);
			}
			if (emptyEnd) { commandArguments.add(""); }
			
			if (commands.getBoxWithObj(baseCommand) != null) {
				TerminalCommand command = commands.getBoxWithObj(baseCommand).getValue();
				
				if (command == null) {
					termIn.error("Unrecognized command.");
					if (!termIn.isChatTerminal()) {	termIn.writeln(); }
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
						if (!termIn.isChatTerminal()) {	termIn.writeln(); }
						drawSpace = true;
					}
				}
				
				return;
			}
		}
		termIn.writeln("Unrecognized command." + (!termIn.isChatTerminal() ? "\n" : ""), 0xff5555);
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
		StorageBoxHolder<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>> sortedAll = getSortedCommands();
		
		for (StorageBox<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>> box : sortedAll) {
			StorageBoxHolder<String, EArrayList<TerminalCommand>> catCommands = box.getValue();
			for (StorageBox<String, EArrayList<TerminalCommand>> byCat : catCommands) {
				for (TerminalCommand command : byCat.getValue()) {
					if (EnhancedMC.isDevMode() || command.showInHelp()) {
						cmds.add(command.getName());
					}
				}
			}
		}
		
		return cmds;
	}
	
	public static StorageBoxHolder<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>> getSortedCommands() {
		StorageBoxHolder<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>> sortedCommands = new StorageBoxHolder();
		EArrayList<TerminalCommand> unsorted = TerminalCommandHandler.getInstance().getCommandList();
		
		//filter out commands that should not be shown in help
		unsorted = unsorted.stream().filter(c -> c.showInHelp() || EnhancedMC.isDevMode()).collect(EArrayList.toEArrayList());
		
		//get command categories
		EArrayList<String> categories = new EArrayList();
		for (TerminalCommand c : unsorted) {
			if (c != null) { categories.addIfNotContains(c.getCategory()); }
		}
		Collections.sort(categories);
		
		//collect for each category
		StorageBoxHolder<String, EArrayList<TerminalCommand>> commandsByCategory = new StorageBoxHolder();
		categories.forEach(c -> commandsByCategory.add(c, null));
		EArrayList<TerminalCommand> toProcess = new EArrayList(unsorted);
		
		for (String category : categories) {
			EArrayList<TerminalCommand> byCat = new EArrayList();
			
			Iterator<TerminalCommand> it = toProcess.iterator();
			while (it.hasNext()) {
				TerminalCommand c = it.next();
				if (c.getCategory().equals(category)) {
					byCat.add(c);
					it.remove();
				}
			}
			
			Collections.sort(byCat, new Sorter());
			commandsByCategory.getBoxWithObj(category).setValue(byCat);
		}
		
		//get command types
		EArrayList<CommandType> types = new EArrayList();
		unsorted.forEach(c -> types.addIfNotContains(c.getType()));
		
		//isolate the 'none' category
		StorageBox<String, EArrayList<TerminalCommand>> noneCat = commandsByCategory.removeBoxesContainingObj("none").get(0);
		EArrayList<TerminalCommand> typeToProcess = new EArrayList(noneCat.getValue());
		
		//filter out commands that have a category but are not normal
		for (StorageBox<String, EArrayList<TerminalCommand>> byCat : commandsByCategory) {
			Iterator<TerminalCommand> it = byCat.getValue().iterator();
			while (it.hasNext()) {
				TerminalCommand c = it.next();
				if (c.getType() != CommandType.NORMAL) {
					StorageBox<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>> box = sortedCommands.getBoxWithObj(c.getType());
					if (box != null) {
						StorageBoxHolder<String, EArrayList<TerminalCommand>> cats = box.getValue();
						StorageBox<String, EArrayList<TerminalCommand>> catBox = cats.getBoxWithObj(c.getCategory());
						if (catBox != null) {
							catBox.getValue().add(c);
						}
						else {
							cats.add(new StorageBox(c.getCategory(), new EArrayList(c)));
						}
					}
					else {
						sortedCommands.add(c.getType(), new StorageBoxHolder(c.getCategory(), new EArrayList(c)));
					}
					it.remove();
				}
			}
		}
		
		//add all other command categories except for 'none'
		sortedCommands.add(CommandType.NORMAL, commandsByCategory);
		
		//parse 'none' category for different command types
		for (CommandType type : types) {
			if (type == CommandType.NORMAL) { continue; }
			EArrayList<TerminalCommand> byType = new EArrayList();
			
			Iterator<TerminalCommand> it = typeToProcess.iterator();
			while (it.hasNext()) {
				TerminalCommand t = it.next();
				if (t.getType() == type) {
					byType.add(t);
					it.remove();
				}
			}
			
			if (sortedCommands.getBoxWithObj(type) == null) {
				sortedCommands.add(type, new StorageBoxHolder("nocat", byType));
			}
		}
		
		//add any remaining commands in the 'none' category to the normal command type
		sortedCommands.getBoxWithObj(CommandType.NORMAL).getValue().add(new StorageBox("No Category", typeToProcess));
		
		//ensure correct command type order
		EArrayList<StorageBox<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>>> commands = sortedCommands.getBoxes();
		sortedCommands.clear();
		Collections.sort(commands, new Comparator<StorageBox<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>>>() {

			@Override
			public int compare(StorageBox<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>> o1, StorageBox<CommandType, StorageBoxHolder<String, EArrayList<TerminalCommand>>> o2) {
				return o1.getObject().compareTo(o2.getObject());
			}
			
		});
		sortedCommands.addAll(commands);
		
		return sortedCommands;
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
