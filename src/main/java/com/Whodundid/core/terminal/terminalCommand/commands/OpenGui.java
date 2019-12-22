package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.guiUtil.GuiOpener;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class OpenGui implements IConsoleCommand {
	
	@Override public String getName() { return "gui"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList("og"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Command used for opening guis."; }
	@Override public String getUsage() { return "ex: gui settings"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		try {
			if (args.isEmpty()) { conIn.info(getUsage()); }
			else if (args.size() >= 1) {
				EArrayList<Class> guis = new EArrayList();
				for (String s : args) { guis.add(RegisteredSubMods.getGuiClassByAlias(s)); }
				
				Object lastGui = null;
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
						conIn.writeln("Opening gui: " + guiClass.getSimpleName(), EColors.green);
					}
					else {
						conIn.error("No gui found under: " + args.get(i));
					}
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
}
