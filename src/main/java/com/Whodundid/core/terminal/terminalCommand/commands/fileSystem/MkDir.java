package com.Whodundid.core.terminal.terminalCommand.commands.fileSystem;

import java.io.File;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class MkDir extends FileCommand {
	
	public MkDir() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}
	
	@Override public String getName() { return "mkdir"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Attempts to create a directory."; }
	@Override public String getUsage() { return "ex: mkdir 'name'"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { defaultTabComplete(termIn, args); }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); }
		if (args.size() == 1) {
			try {
				
				File f = new File(termIn.getDir().getCanonicalPath() + "/" + args.get(0));
				
				if (f.exists()) {
					if (!f.isDirectory()) {
						f.mkdirs();
					}
					else { termIn.error("That directory already exists!"); }
				}
				else {
					f.mkdirs();
				}
				
			} catch (Exception e) { e.printStackTrace(); }
		}
		else {
			termIn.error("Too many arguments!");
		}
	}
}
