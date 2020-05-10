package com.Whodundid.core.terminal.terminalCommand;

import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;

public interface IListableCommand {
	
	public void list(ETerminal conIn, EArrayList<String> args, boolean runVisually);

}
