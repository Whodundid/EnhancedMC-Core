package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.windows.TextureDisplayer;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.DynamicTextureHandler;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class ViewTexture extends TerminalCommand {
	
	public ViewTexture() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}

	@Override public String getName() { return "viewtexture"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return EArrayList.of("vt"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Attempts to display a texture from the specified runtime (resources) path."; }
	@Override public String getUsage() { return "ex: vt /assets/enhancedmc/global/logo.png"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.info(getUsage()); }
		else if (args.size() == 1) {
			parse(termIn, args.get(0));
		}
		else { termIn.error("Too many arguments!"); }
	}
	
	private void parse(ETerminal termIn, String pathIn) {
		String file = pathIn;
		
		try {
			FileSystem filesystem = null;
			URL url = EnhancedMC.class.getResource(file);
			
			if (url != null) {
				URI uri = url.toURI();
				Path path = null;

				if ("file".equals(uri.getScheme())) {
					path = Paths.get(EnhancedMC.class.getResource(file).toURI());
				}
				else {
					filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
					path = filesystem.getPath(file);
				}
				
				Optional<Path> obj = Files.walk(path).findFirst();
				
				Path thePath = obj.get();
				
				if (thePath != null) {
					
					try {
						File dir = thePath.toFile();
						
						try {
							BufferedImage img = ImageIO.read(dir);
							DynamicTextureHandler handler = new DynamicTextureHandler(Minecraft.getMinecraft().renderEngine, img);
							if (handler != null) {
								termIn.writeln("Opening...", EColors.green);
								EnhancedMC.displayWindow(new TextureDisplayer(handler), CenterType.screen);
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
					catch (UnsupportedOperationException e) {
						try {
							//attempt to parse into image
							InputStream stream = Files.newInputStream(thePath);
							if (stream != null) {
								BufferedImage img = ImageIO.read(stream);
								stream.close();
								
								DynamicTextureHandler handler = new DynamicTextureHandler(Minecraft.getMinecraft().renderEngine, img);
								if (handler != null) {
									termIn.writeln("Opening", EColors.green);
									EnhancedMC.displayWindow(new TextureDisplayer(handler), CenterType.screen);
								}
							}
						}
						catch (IIOException | FileSystemException q) {
							termIn.writeln("Path Directory: " + EnumChatFormatting.AQUA + thePath + "\n", EColors.yellow);
							EArrayList<Path> paths = Files.list(thePath).collect(EArrayList.toEArrayList());
							for (Path p : paths) {
								boolean d = Files.isDirectory(p);
								termIn.writeln(p, d ? 0xff2265f0 : EColors.green.intVal);
							}
						}
					}
					
				}
			}
			else {
				termIn.error("Error: File not found!");
			}
			
			if (filesystem != null) { filesystem.close(); }
		}
		catch (Exception e) {
			e.printStackTrace();
			termIn.badError(e.toString());
		}
	}
	
}