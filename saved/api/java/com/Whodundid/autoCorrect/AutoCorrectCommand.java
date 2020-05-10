package com.Whodundid.autoCorrect;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

//Last edited: Oct 15, 2018
//First Added: Oct 14, 2018
//Author: Hunter Bragg

public class AutoCorrectCommand {
	
	static Minecraft mc = Minecraft.getMinecraft();
	private String baseCommand = "";
	private ArrayList<String> aliases;
	private CommandBase commandMC;
	
	public AutoCorrectCommand(String baseCommandIn) { this(baseCommandIn, new ArrayList<String>()); }
	public AutoCorrectCommand(String baseCommandIn, ArrayList<String> aliasesIn) {
		baseCommand = baseCommandIn;
		aliases = aliasesIn;
		buildCommand();
	}
	
	public synchronized AutoCorrectCommand addAlias(String aliasIn) {
		if (!aliases.contains(aliasIn)) { aliases.add(aliasIn); }
		return this;
	}
	
	public synchronized AutoCorrectCommand addAliases(ArrayList<String> aliasesIn) {
		for (String s : aliasesIn) { addAlias(s); }
		return this;
	}
	
	public synchronized AutoCorrectCommand addAliases(String... aliases) {
		for (String s : aliases) { addAlias(s); }
		return this;
	}
	
	public synchronized AutoCorrectCommand removeAlias(String aliasIn) {
		if (aliases.contains(aliasIn)) {
			Iterator<String> it = aliases.iterator();
			while (it.hasNext()) {
				if (aliasIn.equals(it.next())) { it.remove(); }
			}
		}
		return this;
	}
	
	private void buildCommand() {
		commandMC = new CommandBase() {
			@Override public String getCommandName() { return baseCommand; }
			@Override public int getRequiredPermissionLevel() { return 0; }
			@Override public String getCommandUsage(ICommandSender sender) { return "Autocorrect cmd for: " + baseCommand; }
			@Override public void processCommand(ICommandSender sender, String[] args) throws CommandException {
				if (mc.thePlayer != null) { mc.thePlayer.sendChatMessage(baseCommand); }
			}
		};
	}
	
	public String getBaseCommand() { return baseCommand; }
	public ArrayList<String> getAliases() { return aliases; }
	public synchronized AutoCorrectCommand clearAliases() { aliases.clear(); return this; }
	public CommandBase getCommandMC() { return commandMC; }
}
