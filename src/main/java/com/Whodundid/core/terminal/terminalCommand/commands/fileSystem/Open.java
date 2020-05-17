package com.Whodundid.core.terminal.terminalCommand.commands.fileSystem;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.windows.TextEditorWindow;
import com.Whodundid.core.enhancedGui.guiObjects.windows.TextureDisplayer;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import net.minecraft.util.EnumChatFormatting;

public class Open extends FileCommand {
	
	public Open() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}
	
	@Override public String getName() { return "open"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Used to open or run a file or application."; }
	@Override public String getUsage() { return "ex: open 'file'"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { defaultTabComplete(termIn, args); }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); termIn.info(getUsage()); }
		else if (args.size() >= 1) {
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
									open(termIn, f);
								}
								catch (Exception e) {
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
	
	private void check(ETerminal termIn, File path) {
		if (path.isDirectory()) {
			try {
				termIn.setDir(new File(path.getCanonicalPath()));
				termIn.info("Current Dir: " + EnumChatFormatting.AQUA + EnumChatFormatting.UNDERLINE + termIn.getDir().getCanonicalPath());
			} catch (IOException e) { e.printStackTrace(); }
		}
		else { open(termIn, path); }
	}
	
	private void open(ETerminal termIn, File dir) {
		try {
			String path = dir.getAbsolutePath().trim();
			
			if (dir.exists()) {
				if (path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".gif") || path.endsWith(".tga") || path.endsWith(".bmp")) {
					try {
						BufferedImage img = ImageIO.read(dir);
						if (img != null) {
							termIn.writeln("Opening...", EColors.green);
							EnhancedMC.displayWindow(new TextureDisplayer(dir), CenterType.screen);
						}
					}
					catch (IIOException e) {
						File[] files = dir.listFiles();
						
						termIn.writeln("File Directory: " + EnumChatFormatting.AQUA + dir + "\n", EColors.yellow);
						
						for (File f : files) {
							boolean d = f.isDirectory();
							termIn.writeln(f + (d ? "\\" : ""), d ? 0xff2265f0 : EColors.green.intVal);
						}
						
					}
				}
				else if (path.endsWith(".txt") || path.endsWith(".cfg")) {
					if (path != null) {
						termIn.info("Opening edit window..");
						
						TextEditorWindow window = new TextEditorWindow(dir);
						window.setFocusedObjectOnClose(termIn);
						
						EnhancedMC.displayWindow(window, CenterType.screen);
						
						window.setFocusToLineIfEmpty();
					}
				}
				else if (dir.canExecute()) {
					try {
						EUtil.openFile(dir);
					}
					catch (Exception e) { e.printStackTrace(); }
				}
			}
			else { termIn.error("No file/directory with that name found in the current directory!"); }
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
