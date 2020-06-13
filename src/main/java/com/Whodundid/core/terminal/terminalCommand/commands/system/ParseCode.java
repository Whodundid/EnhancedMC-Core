package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ParseCode extends TerminalCommand {
	
	public ParseCode() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 1;
	}

	@Override public String getName() { return "parsecode"; }
	@Override public boolean showInHelp() { return false; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return ""; }
	@Override public String getUsage() { return ""; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		
		if (args.isEmpty()) { termIn.error("Unrecognized command."); }
		else if (args.size() >= 1) {
			try {
				if (args.get(0) != null && !args.get(0).isEmpty()) {
					String input = EUtil.combineAll(args, " ");
					String decoded = "";
					for (int i = 0; i < input.length(); i++) {
						char g = input.charAt(i);
						int val = (i % 2 == 0 ? g + 7 : g - 13);
						if (val < 32) { val = 126 - (32 - val); }
						if (val > 126) { val = 32 + (val - 126); }
						char l = (char) val;
						if (i == 0 && l == ' ') { l = 'B'; }
						if (i == input.length() - 1 && l == ' ') { l = 'S'; }
						decoded += l;
					}
					termIn.writeln(decoded, EColors.pink);
				}
				else { termIn.error("Unrecognized command."); }
			}
			catch (Exception e) { termIn.error("Unrecognized command."); }
		}
	}
	
}
