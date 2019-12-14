package com.Whodundid.core.debug.terminal.terminalCommand;

import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;

public interface IConsoleCommand {
	public String getName();	
	public EArrayList<String> getAliases();
	public String getHelpInfo(boolean runVisually);
	public String getUsage();
	public EArrayList<String> getTabCompleteList();
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually);
}
