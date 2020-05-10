package com.Whodundid.core.terminal.terminalCommand.commands.fileSystem;

import com.Whodundid.core.terminal.TerminalCommandHandler;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.io.File;

public class Lsblk extends FileCommand {
	
	TerminalCommandHandler handler;
	
	public Lsblk() {
		super(CommandType.NORMAL);
		numArgs = 0;
	}
	
	@Override public String getName() { return "lsblk"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Prints the current drives on the system."; }
	@Override public String getUsage() { return "ex: lsblk"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		try {
			if (args.size() == 0) {
				File[] drives = File.listRoots();
				if (drives != null && drives.length > 0) {
					String title = String.format("%-10s%-22s%-30s", "Drive", "Total size (gb)", "Free Space (gb)");
					termIn.writeln(title, 0xff2265f0);
					termIn.writeln(EUtil.repeatString("-", title.length() - 22), 0xff2265f0);
				    for (File f : drives) {
				    	String out = String.format("%-11s%-23.2f%-30.2f", f.getCanonicalPath(),
				    													  (double) (f.getTotalSpace() / (double) 1000000000),
				    													  (double) (f.getFreeSpace() / (double) 1000000000));
				        termIn.writeln(out, 0xff2265f0);
				    }
				}
				else {
					termIn.badError("There appear to be no drives available on the system!");
				}
			}
			else { termIn.error("Too many arguments!"); }
		}
		catch (Exception e) { e.printStackTrace(); }
	}
}
