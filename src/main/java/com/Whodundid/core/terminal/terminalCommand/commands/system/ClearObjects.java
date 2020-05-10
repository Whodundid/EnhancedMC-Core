package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class ClearObjects extends TerminalCommand {
	
	public ClearObjects() {
		super(CommandType.NORMAL);
		numArgs = 0;
	}

	@Override public String getName() { return "clearobj"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("clro"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the objects from the renderer"; }
	@Override public String getUsage() { return "ex: clro"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		EnhancedMCRenderer ren = EnhancedMCRenderer.getInstance();
		EArrayList<IEnhancedGuiObject> objs = EArrayList.combineLists(ren.getObjects(), ren.getAddingObjects());
		if (objs.contains(termIn)) { objs.remove(termIn); }
		if (objs.isNotEmpty()) {
			termIn.writeln("Closing Renderer Objects..", 0x00ffff);
			for (IEnhancedGuiObject o : objs) {
				if (o.isCloseable()) {
					if (runVisually) { termIn.writeln("Closing: " + o, 0xffff00); }
					o.close();
				}
			}
			if (runVisually) { termIn.writeln(objs.size() + " closed.", 0xffaa00); }
		}
	}
}
