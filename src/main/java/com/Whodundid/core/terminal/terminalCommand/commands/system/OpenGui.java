package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.debug.ExperimentGui;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.guiUtil.GuiOpener;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiSnooper;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.ScreenChatOptions;

//Author: Hunter Bragg

public class OpenGui extends TerminalCommand {
	
	public OpenGui() {
		super(CommandType.NORMAL);
		numArgs = -1;
	}
	
	@Override public String getName() { return "opengui"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList("gui", "og"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Command used for opening guis."; }
	@Override public String getUsage() { return "ex: og settings"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		EArrayList<String> options = new EArrayList();
		String[] vanilla = {"chat", "pause", "main", "mainmenu", "multiplayer", "skin", "resourcepacks", "options", "video", "sound", "soundoptions", "snooper", "language"};
		options.addAll(termIn.getAliases());
		options.addAll(ExperimentGui.aliases);
		options.addA(vanilla);
		options.addAll(RegisteredApps.getAllGuiClasses().stream().map(c -> c.getSimpleName()).collect(EArrayList.toEArrayList()));
		
		for (EMCApp a : RegisteredApps.getAppsList()) {
			for (IWindowParent p : a.getGuis()) {
				options.addAll(p.getAliases());
			}
		}
		
		super.basicTabComplete(termIn, args, options);
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		try {
			if (args.isEmpty()) { termIn.info(getUsage()); }
			else if (args.size() >= 1) {
				EArrayList<Class> guis = new EArrayList();
				for (String s : args) {
					s = s.toLowerCase();
					
					if (EUtil.findMatch(s, termIn.getAliases())) { guis.addIfNotNull(termIn.getClass()); }
					else if (EUtil.findMatch(s, ExperimentGui.aliases)) { guis.addIfNotNull(ExperimentGui.class); }
					else {
						Class found = RegisteredApps.getGuiClassByAlias(s);
						guis.addIfNotNull(found);
						
						if (found == null) {
							for (Class c : RegisteredApps.getAllGuiClasses()) {
								String name = c.getSimpleName().toLowerCase();
								if (name.equals(s)) { guis.add(c); }
							}
						}
					}			
				}
				guis.addAll(checkForVanillaGuis(args));
				
				Object lastGui = null;
				if (guis.isNotEmpty()) {
					for (int i = 0; i < guis.size(); i++) {
						Class guiClass = guis.get(i);
						if (guiClass != null) {
							if (lastGui == null) {
								lastGui = GuiOpener.openGui(guiClass, CenterType.screen);
							}
							else {
								if (lastGui instanceof IWindowParent) {
									lastGui = GuiOpener.openGui(guiClass, (IWindowParent) lastGui, CenterType.objectIndent);
								}
								else {
									GuiOpener.openGui(guiClass, CenterType.screen);
								}
							}
							termIn.writeln("Opening gui: " + guiClass.getSimpleName(), EColors.green);
						}
					}
					
					//update the taskbar -- if it exists
					EUtil.ifNotNullDo(EnhancedMCRenderer.getInstance().getTaskBar(), b -> b.forceUpdate());
					
				}
				else {
					termIn.error("No guis found");
				}
			}
		}
		catch (Exception e) {
			termIn.badError(e.toString());
			e.printStackTrace();
		}
	}
	
	private EArrayList<Class> checkForVanillaGuis(EArrayList<String> args) {
		EArrayList<Class> guis = new EArrayList();
		
		for (String s : args) {
			switch (s) {
			case "chat": guis.add(GuiChat.class); break;
			case "pause": guis.add(GuiIngameMenu.class); break;
			case "main":
			case "mainmenu": guis.add(GuiMainMenu.class); break;
			case "multiplayer": guis.add(GuiMultiplayer.class); break;
			case "skin": guis.add(GuiCustomizeSkin.class); break;
			case "resourcepacks": guis.add(GuiScreenResourcePacks.class); break;
			case "options": guis.add(GuiOptions.class); break;
			case "video": guis.add(GuiVideoSettings.class); break;
			case "chatoptions": guis.add(ScreenChatOptions.class); break;
			case "sound": guis.add(GuiScreenOptionsSounds.class); break;
			case "snooper": guis.add(GuiSnooper.class); break;
			case "language": guis.add(GuiLanguage.class); break;
			}
		}
		
		return guis;
	}
	
}
