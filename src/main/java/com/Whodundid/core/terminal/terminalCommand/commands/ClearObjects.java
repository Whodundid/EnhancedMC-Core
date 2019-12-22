package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ClearObjects implements IConsoleCommand {

	@Override public String getName() { return "clearobj"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("clro"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the objects from the renderer"; }
	@Override public String getUsage() { return "ex: clro"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		EnhancedMCRenderer ren = EnhancedMCRenderer.getInstance();
		EArrayList<IEnhancedGuiObject> objs = EArrayList.combineLists(ren.getObjects(), ren.getAddingObjects());
		if (objs.contains(conIn)) { objs.remove(conIn); }
		if (objs.isNotEmpty()) {
			conIn.writeln("Closing Renderer Objects..", 0x00ffff);
			for (IEnhancedGuiObject o : objs) {
				if (o.isCloseable()) {
					if (runVisually) { conIn.writeln("Closing: " + o, 0xffff00); }
					o.close();
				}
			}
			if (runVisually) { conIn.writeln(objs.size() + " closed.", 0xffaa00); }
		}
	}
}

