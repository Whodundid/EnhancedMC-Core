package com.Whodundid.core.terminal.terminalCommand.commands.fileSystem;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.windows.TextEditorWindow;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.io.File;

public class Edit extends FileCommand {
	
	public Edit() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}
	
	@Override public String getName() { return "edit"; }
	@Override public boolean showInHelp() { return EnhancedMC.isOpMode(); }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Used to edit the contents of a file."; }
	@Override public String getUsage() { return "ex: edit 'file'"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { defaultTabComplete(termIn, args); }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (EnhancedMC.isOpMode()) {
			if (args.isEmpty()) { termIn.error("Not enough arguments!"); }
			else if (args.size() == 1) {
				try {
					String all = EUtil.combineAll(args, " ");
					File f = new File(termIn.getDir(), all);
					
					if (all.startsWith("..")) { f = new File(termIn.getDir(), args.get(0)); }
					if (args.get(0).equals("~")) { f = new File(System.getProperty("user.dir")); }
					
					if (f.exists()) { check(termIn, f); }
					else {
						f = new File(all);
						
						if (f.exists()) { check(termIn, f); }
						else {
							if (args.get(0).startsWith("..")) { f = new File(termIn.getDir(), args.get(0)); }
							else { f = new File(args.get(0)); }
							
							if (f.exists()) { check(termIn, f); }
							else {
								f = new File(termIn.getDir(), args.get(0));
								
								if (f.exists()) { check(termIn, f); }
								else {
									try {
										openEditWindow(termIn, f);
									} catch (Exception e) {
										e.printStackTrace();
										termIn.error("'" + args.get(0) + "' is not a vaild file!");
									}
								}
							}
						}
					}
					
				} catch (Exception e) { e.printStackTrace(); }
			}
			else { termIn.error("Too many arguments!"); }
		}
		else { termIn.error("Unrecognized Command!"); }
	}
	
	private void check(ETerminal termIn, File path) {
		if (path.isDirectory()) { termIn.error("Error: " + path.getName() + " is a directory!"); }
		else { openEditWindow(termIn, path); }
	}
	
	private void openEditWindow(ETerminal termIn, File path) {
		if (path != null) {
			termIn.info("Opening edit window..");
			
			TextEditorWindow window = new TextEditorWindow(path);
			window.setFocusedObjectOnClose(termIn);
			
			EnhancedMC.displayWindow(window, CenterType.screen);
			
			window.setFocusToLineIfEmpty();
		}
	}

}
