package com.Whodundid.core.debug.console.commands;

import com.Whodundid.core.debug.console.gui.EConsole;
import com.Whodundid.core.util.storageUtil.EArrayList;

public interface IConsoleCommand {
	public String getName();	
	public EArrayList<String> getAliases();
	public String getCommandHelpInfo();
	public String getCommandErrorInfo(String arg);
	public void runCommand(EConsole conIn, EArrayList<String> args, boolean runVisually);
}
