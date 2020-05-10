package com.Whodundid.core.terminal.terminalCommand.commands.fileSystem;

import java.io.File;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class Rm extends FileCommand {
	
	public Rm() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}
	
	@Override public String getName() { return "rm"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Deletes a file from the file system."; }
	@Override public String getUsage() { return "ex: rm 'dir'"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { defaultTabComplete(termIn, args); }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); }
		if (args.size() == 1) {
			try {
				File f = new File(termIn.getDir().getCanonicalPath() + "/" + args.get(0));
				
				if (f.exists()) {
					if (!f.isDirectory()) {
						if (!f.delete()) { termIn.error("Error: cannot delete the specified file"); }
						termIn.info("Removing file");
					}
					else {
						termIn.error(f.getName() + " is a directory!");
					}
				}
				else {
					termIn.error("Error: Cannot find a file with that path!");
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
		else if (args.size() == 2) {
			try {
				File f = new File(termIn.getDir().getCanonicalPath() + "/" + args.get(0));
				
				if (f.exists()) {
					if (f.isDirectory()) {
						if (args.get(1).equals("-r")) {
							if (!deleteRecursively(termIn, f)) { termIn.error("Error: cannot fully delete specified directory"); }
							termIn.info("Removing directory");
						}
						else {
							termIn.error("Invalid parameter type: '" + args.get(0) + "'");
						}
					}
					else {
						if (!f.delete()) { termIn.error("Error: cannot delete the specified file"); }
						termIn.info("Removing file");
					}
				}
				else {
					termIn.error("Error: Cannot find a file with that path!");
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
		else if (args.size() > 2) {
			termIn.error("Too many arguments!");
		}
	}
	
}
