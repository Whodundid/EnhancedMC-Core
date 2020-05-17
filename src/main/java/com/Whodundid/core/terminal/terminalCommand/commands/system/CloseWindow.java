package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class CloseWindow extends TerminalCommand {
	
	public CloseWindow() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}

	@Override public String getName() { return "closewindow"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("clw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Attempts to close a specific window"; }
	@Override public String getUsage() { return "ex: clw 23 (where 23 is the window pid)"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); termIn.info(getUsage()); }
		else if (args.size() == 1) {
			try {
				long pid = Long.parseLong(args.get(0));
				EArrayList<WindowParent> windows = EnhancedMC.getAllActiveWindows();
				
				WindowParent theWindow = EUtil.getFirst(windows, w -> w.getObjectID() == pid);
				if (EUtil.ifNotNullDo(theWindow, w -> w.close())) {
					termIn.writeln("Window: '" + theWindow.getObjectName() + " ; " + theWindow.getObjectID() + "' closed", EColors.green);
				}
				else { termIn.error("No window with that pid currently exists!"); }
				
			}
			catch (Exception e) {
				termIn.error("Failed to parse input!");
				e.printStackTrace();
			}
		}
		else {
			termIn.error("Not enough arguments!");
		}
	}
	
}