package com.Whodundid.core.terminal.terminalCommand.commands.fileSystem;

import java.io.IOException;
import net.minecraft.util.EnumChatFormatting;
import com.Whodundid.core.terminal.TerminalCommandHandler;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class Pwd extends TerminalCommand {
	
	TerminalCommandHandler handler;
	
	public Pwd() {
		super(CommandType.NORMAL);
		numArgs = 0;
	}
	
	@Override public String getName() { return "pwd"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Prints the current working directory."; }
	@Override public String getUsage() { return "ex: pwd"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		try {
			if (args.size() == 0) { termIn.info("Current Dir: " + EnumChatFormatting.AQUA + EnumChatFormatting.UNDERLINE + termIn.getDir().getCanonicalPath()); }
			else { termIn.error("Too many arguments!"); }
		} catch (IOException e) { e.printStackTrace(); }
	}
}
