package com.Whodundid.core.terminal.terminalCommand.commands.fileSystem;

import java.io.File;
import java.io.IOException;
import net.minecraft.util.EnumChatFormatting;
import com.Whodundid.core.terminal.TerminalCommandHandler;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class Pwd extends FileCommand {
	
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
		if (args.size() == 0) {
			try {
				String path = termIn.getDir().getCanonicalPath();
				String colorPath = "" + EnumChatFormatting.AQUA + EnumChatFormatting.UNDERLINE + path + EnumChatFormatting.RESET;
				termIn.writeLink("Current Dir: " + colorPath, path, new File(path), false, EColors.yellow);
			}
			catch (IOException e) {
				error(termIn, e);
			}
		}
		else { termIn.error("Too many arguments!"); }
	}
	
}

