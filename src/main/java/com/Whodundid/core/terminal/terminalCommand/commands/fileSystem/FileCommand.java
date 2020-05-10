package com.Whodundid.core.terminal.terminalCommand.commands.fileSystem;

import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.io.File;

public abstract class FileCommand extends TerminalCommand {

	public FileCommand(CommandType typeIn) {
		super(typeIn);
		setCategory("File System");
	}
	
	protected void defaultTabComplete(ETerminal termIn, EArrayList<String> args) {
		try {
			if (args.isEmpty()) {
				File f = new File(termIn.getDir().getCanonicalPath());
				
				EArrayList<String> options = new EArrayList();
				
				for (File file : f.listFiles()) {
					options.add(file.getName());
				}
				
				termIn.buildTabCompletions(options);
				if (!termIn.getTab1()) { termIn.setTextTabBeing(""); }
			}
			else {
				File f = new File(termIn.getDir().getCanonicalPath());
				
				File testPath = new File(args.get(termIn.getCurrentArg() - 1));
				//System.out.println(testPath);
				if (testPath.exists()) { f = testPath; termIn.setTextTabBeing(args.get(0)); }
				
				String search = !termIn.getTab1() ? args.get(0) : termIn.getTextTabBegin();
				
				EArrayList<String> options = new EArrayList();
				
				if (f != null && f.listFiles() != null) {
					for (File file : f.listFiles()) {
						String fName = file.getName().toLowerCase();
						String check = search.toLowerCase();
						
						if (fName.startsWith(check)) {
							options.add(file.getName());
						}
					}
				}
				
				termIn.buildTabCompletions(options);
				if (!termIn.getTab1()) { termIn.setTextTabBeing(args.get(0)); }
			}
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	protected boolean deleteRecursively(ETerminal termIn, File path) {
		File[] allContents = path.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteRecursively(termIn, file);
	        }
	    }
	    return path.delete();
	}

}
