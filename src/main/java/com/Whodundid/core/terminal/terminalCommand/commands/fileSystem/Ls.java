package com.Whodundid.core.terminal.terminalCommand.commands.fileSystem;

import java.io.File;
import java.io.IOException;
import net.minecraft.util.EnumChatFormatting;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class Ls extends FileCommand {
	
	public Ls() {
		super(CommandType.NORMAL);
		setModifiers("-a");
		numArgs = 1;
	}
	
	@Override public String getName() { return "ls"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "List all files in a directory."; }
	@Override public String getUsage() { return "ex: ls 'dir'"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { defaultTabComplete(termIn, args); }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { listDir(termIn, false, null); }
		else if (args.size() >= 1) {
			File theFile = null;
			boolean hidden = false;
			
			String all = EUtil.combineAll(args, " ");
			EArrayList<String> allArgs = new EArrayList(args);
			allArgs.add(all);
			
			for (String s : allArgs) {
				if (s.startsWith("-")) {
					if (checkForModifier(s)) {
						hidden = s.equals("-a");
					}
					else {
						termIn.error("Invalid modifier: '" + s + "'");
						return;
					}
				}
				else {
					theFile = new File(s);
					if (!theFile.exists()) {
						theFile = new File(termIn.getDir(), s);
					}
				}
			}
			
			if (args.get(0).equals("~")) { theFile = new File(System.getProperty("user.dir")); }
			
			listDir(termIn, hidden, theFile);
		}
		else if (args.size() > modifiers.size() + 1) {
			termIn.error("Too many arguments!");
		}
	}
	
	private void listDir(ETerminal termIn, boolean showHidden, File dir) {
		if (dir == null) { dir = termIn.getDir(); }
		
		if (dir.exists()) {
			
			if (!dir.isDirectory()) { termIn.error(dir.getName() + " is a file, not a directory!"); return; }
			
			try {
				termIn.info("Viewing Dir: " + EnumChatFormatting.AQUA + EnumChatFormatting.UNDERLINE + dir.getCanonicalPath());
			}
			catch (IOException e) { e.printStackTrace(); }
			if (dir.list().length > 0) { termIn.writeln(); }
			
			for (String s : dir.list()) {
				File f = new File(dir, s);
				
				//System.out.printf("%-50s%-100s%s\n", s, f, f.isDirectory());
				
				if (f.isHidden() && !showHidden) { continue; }
				
				if (f.isDirectory()) { termIn.writeln(f.getName() + "/", 0xff2265f0); }
				else { termIn.writeln(s, EColors.green); }
			}
		}
		else {
			try {
				termIn.error("'" + dir.getCanonicalPath() + "' is not a vaild directory!");
			}
			catch (IOException e) { e.printStackTrace(); }
		}
	}
	
}
