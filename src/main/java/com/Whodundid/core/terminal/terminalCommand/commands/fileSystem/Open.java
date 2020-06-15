package com.Whodundid.core.terminal.terminalCommand.commands.fileSystem;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowObjects.windows.TextEditorWindow;
import com.Whodundid.core.windowLibrary.windowObjects.windows.TextureDisplayer;
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
	@Override public String getHelpInfo(boolean runVisually) { return "Used to open or run a file or application. Add -a to end to always open on computer."; }
	@Override public String getUsage() { return "ex: open 'file'"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { fileTabComplete(termIn, args); }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); termIn.info(getUsage()); }
		else if (args.size() >= 1) {
			try {
				boolean openA = args.getLast().equals("-a");
				String all = "";
				
				if (openA) { all = EUtil.combineAll(args.subList(0, args.size() - 1), " "); }
				else { all = EUtil.combineAll(args, " "); }
				
				File f = new File(termIn.getDir(), all);
				
				if (all.startsWith("..")) { f = new File(termIn.getDir(), args.get(0)); }
				if (args.get(0).equals("~")) { f = new File(System.getProperty("user.dir")); }
				
				if (f.exists()) { check(termIn, f, openA); }
				else {
					f = new File(all);
					
					if (f.exists()) { check(termIn, f, openA); }
					else {
						if (args.get(0).startsWith("..")) { f = new File(termIn.getDir(), args.get(0)); }
						else { f = new File(args.get(0)); }
						
						if (f.exists()) { check(termIn, f, openA); }
						else {
							f = new File(termIn.getDir(), args.get(0));
							
							if (f.exists()) { check(termIn, f, openA); }
							else {
								try {
									open(termIn, f, openA);
								}
								catch (Exception e) {
									termIn.error("'" + args.get(0) + "' is not a vaild file!");
									error(termIn, e);
								}
							} //else
						}
					} //else
				}
				
			}
			catch (Exception e) { error(termIn, e); }
		}
	}
	
	private void check(ETerminal termIn, File path, boolean openA) {
		if (path.isDirectory()) {
			try {
				String link = path.getCanonicalPath();
				String colorPath = "" + EnumChatFormatting.AQUA + EnumChatFormatting.UNDERLINE + link + EnumChatFormatting.RESET;
				
				if (openA) {
					termIn.writeLink("Opening Dir: " + colorPath, link, new File(link), false, EColors.yellow);
					EUtil.openFile(path);
				}
				else {
					termIn.setDir(new File(path.getCanonicalPath()));
					termIn.writeLink("Current Dir: " + colorPath, link, new File(link), false, EColors.yellow);
				}
			}
			catch (IOException e) { error(termIn, e); }
		}
		else { open(termIn, path, openA); }
	}
	
	private void open(ETerminal termIn, File dir, boolean openA) {
		try {
			if (dir.exists()) {
				if (openA) {
					termIn.writeln("Opening the file on computer...", EColors.lgreen);
					EUtil.openFile(dir);
				}
				else {
					String path = dir.getAbsolutePath().trim();
					
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
							
							error(termIn, e);
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
						catch (Exception e) { error(termIn, e); }
					}
				}
			}
			else { termIn.error("No file/directory with that name found in the current directory!"); }
			
		}
		catch (Exception e) { error(termIn, e); }
	}

}
