package com.Whodundid.autoCorrect;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.Level;

//Last edited: Oct 22, 2018
//First Added: Oct 14, 2018
//Author: Hunter Bragg

//@Mod(modid = AutoCorrectMod.MODID, version = AutoCorrectMod.VERSION, name = AutoCorrectMod.NAME, dependencies = "required-after:enhancedmc")
public class AutoCorrectApp extends EMCApp {
	
	public static final String MODID = "autocorrect";
	public static final String VERSION = "0.1";
	public static final String NAME = "CMD Auto Correct";
	protected ArrayList<AutoCorrectCommand> commands;
	public BuiltInAutoCorrectCommands builtIn;
	public CommandSaver saver;
	public CommandLoader loader;
	private static AutoCorrectApp instance;
	
	public AutoCorrectApp() {
		super(AppType.AUTOCORRECT);
		instance = this;
		shouldLoad = false;
		
		addDependency(AppType.CORE, "1.0");
		setMainGui(new AutoCorrectGui());
		setAliases("autocorrect", "cmdac", "ac");
		
		commands = new ArrayList();
		saver = new CommandSaver(this);
		loader = new CommandLoader(this);
	}
	
	public static AutoCorrectApp instance() { return instance; }
	
	@Override
	public void onPostInit(FMLPostInitializationEvent e) {
		unloadAllCommandsFromRegistry();
		if (!loader.loadCommands()) {
			EnhancedMC.log(Level.WARN, "----------------------------------------------- EMC AC failed");
			EnhancedMC.log(Level.WARN, "Could not load command list. Loading default autocorrect commands.");
			for (int i = 0; i < 10; i++) { System.out.println(); }
			builtIn = new BuiltInAutoCorrectCommands(this);
			builtIn.registerCommands();
			saver.saveCommands();
		}
		registerCommands();
	}
	
	public synchronized boolean addCommand(AutoCorrectCommand commandIn) {
		//System.out.println(commandIn.getBaseCommand());
		return !doesCommandExist(commandIn) ? commands.add(commandIn) : false;
	}
	
	public boolean doesCommandExist(AutoCorrectCommand commandIn) {
		for (AutoCorrectCommand c : commands) {
			if (c.getBaseCommand().equals(commandIn.getBaseCommand())) { return true; }
		}
		return false;
	}
	
	public synchronized AutoCorrectCommand removeCommand(String commandName) { return removeCommand(getCommand(commandName)); }
	
	public synchronized AutoCorrectCommand removeCommand(AutoCorrectCommand commandIn) {
		if (commandIn != null && doesCommandExist(commandIn)) {
			Iterator<AutoCorrectCommand> it = commands.iterator();
			while (it.hasNext()) {
				AutoCorrectCommand c = it.next();
				if (commandIn.getBaseCommand().equals(c.getBaseCommand())) {
					it.remove();
					return c;
				}
			}
		}
		return null;
	}
	
	public synchronized AutoCorrectCommand getCommand(String commandName) {
		for (AutoCorrectCommand c : commands) {
			if (c.getBaseCommand().equals(commandName)) { return c; }
		}
		return null;
	}
	
	public AutoCorrectApp registerCommands() {
		ClientCommandHandler h = ClientCommandHandler.instance;
		Map<String, ICommand> map = h.getCommands();
		synchronized (map) {
			for (AutoCorrectCommand c : commands) {
				for (String alias : c.getAliases()) {
					if (!map.containsKey(alias)) {
						map.put(alias, c.getCommandMC());
						EnhancedMC.log(Level.INFO, "Registering Command Alias: " + alias);
					} else { EnhancedMC.log(Level.WARN, "Alias: '" + alias + "' already exists!"); }
				}
			}
		}
		return this;
	}
	
	public AutoCorrectApp unloadCommandFromRegistry(AutoCorrectCommand commandIn) {
		if (doesCommandExist(commandIn)) {
			ClientCommandHandler h = ClientCommandHandler.instance;
			Map<String, ICommand> map = h.getCommands();
			synchronized (map) {
				for (String alias : commandIn.getAliases()) {
					Iterator<Entry<String, ICommand>> it = map.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, ICommand> cmd = it.next();
						if (cmd.getKey().equals(alias) && cmd.getValue().equals(commandIn.getCommandMC())) { it.remove(); }
					}
				}
				commands.remove(commandIn);
			}
		}
		return this;
	}
	
	public AutoCorrectApp unloadAllCommandsFromRegistry() {
		ClientCommandHandler h = ClientCommandHandler.instance;
		Map<String, ICommand> map = h.getCommands();
		synchronized (map) {
			for (AutoCorrectCommand c : commands) {
				for (String alias : c.getAliases()) {
					Iterator<Entry<String, ICommand>> it = map.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, ICommand> cmd = it.next();
						if (cmd.getKey().equals(alias) && cmd.getValue().equals(c.getCommandMC())) {
							it.remove();
						}
					}
				}
			}
			commands.clear();
		}
		return this;
	}
	
	public List<AutoCorrectCommand> getCommandList() { return Collections.unmodifiableList(commands); }
	
	public class CommandAlreadyExistsException extends Exception {}
}
