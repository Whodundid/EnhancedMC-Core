package com.Whodundid.core.terminal.terminalCommand.commands.fileSystem;

import java.io.File;
import com.Whodundid.core.terminal.TerminalCommandHandler;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class RmDir extends FileCommand {
	
	TerminalCommandHandler handler;
	
	public RmDir() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}
	
	@Override public String getName() { return "rmdir"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Attempts to delete a directory."; }
	@Override public String getUsage() { return "ex: rmdir 'dir'"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { defaultTabComplete(termIn, args); }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); }
		if (args.size() == 1) {
			try {
				File f = new File(termIn.getDir().getCanonicalPath() + "/" + args.get(0));
				
				if (f.exists()) {
					if (!f.isDirectory()) {
						if (!f.delete()) { termIn.error("Error: specified path is not a directory!"); }
					}
					else {
						if (f.listFiles().length > 0) { termIn.error("Error: directory is not empty!"); }
						else if (!f.delete()) { termIn.error("Error: cannot fully delete specified directory"); }
						else if (runVisually) { termIn.info("Removing directory"); }
					}
				}
				else {
					termIn.error("Error: Cannot find a directory with that name!");
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
		else {
			termIn.error("Too many arguments!");
		}
	}
	
}
