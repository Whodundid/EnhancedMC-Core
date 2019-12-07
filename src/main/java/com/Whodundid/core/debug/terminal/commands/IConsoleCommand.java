package com.Whodundid.core.debug.terminal.commands;

import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;

public interface IConsoleCommand {
	public String getName();	
	public EArrayList<String> getAliases();
	public String getCommandHelpInfo();
	public String getCommandErrorInfo(String arg);
	public EArrayList<String> getTabCompleteList();
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually);
}
