package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.mathUtil.NumberUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ForLoop implements IConsoleCommand {

	@Override public String getName() { return "for"; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Runs a command n number of times in given range replacing any 'x' arguments with current value."; }
	@Override public String getUsage() { return "ex: for 0-9 server ping 192.168.0.*"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() < 2) { conIn.error("Not enought arguments for loop!"); conIn.info(getUsage()); }
		else if (args.size() >= 2) {
			String vals = args.get(0);
			
			EArrayList<String> otherArgs = new EArrayList(args.subList(1, args.size()));
			
			//String cmd = "";
			//for (String s : otherArgs) { cmd += (s + " "); }
			//if (otherArgs.size() >= 1) { cmd = cmd.substring(0, cmd.length() - 1); }
			
			if (vals.length() < 3) { conIn.error("Not enough arguments for loop!"); }
			else if (vals.length() >= 3) {
				int pos = EUtil.findStartingIndex(vals, "-");
				String firstArg = vals.substring(0, pos);
				String secondArg = vals.substring(pos + 1);
				
				boolean positive = true;
				int firstI = 0;
				int secondI = 0;
				
				Class type = checkClasses(conIn, firstArg, secondArg);
				
				if (type == null) { conIn.error("Could not parse range value types!"); return; }
				if (type == Exception.class) { conIn.error("Inconsistent range value datatypes!"); return; }
				
				if (type == Integer.class) {
					firstI = Integer.parseInt(firstArg);
					secondI = Integer.parseInt(secondArg);
					positive = (secondI - firstI > 0); //check direction
					
					if (positive) {
						try {
							for (int i = firstI; i <= secondI; i++) {
								runLoop(conIn, i, otherArgs);
							}
						} catch (Exception e) {
							conIn.badError("Java Error: " + e);
							e.printStackTrace();
						}
					}
					else {
						try {
							for (int i = firstI; i >= secondI; i--) {
								runLoop(conIn, i, otherArgs);
							}
						} catch (Exception e) {
							conIn.badError("Java Error: " + e);
							e.printStackTrace();
						}
					}
				}
				else if (type == String.class) {
					
				}
			}
			
			/*
			if (!cmd.isEmpty()) {
				try {
					if (!(cmd.equals("clear") || cmd.equals("clr") || cmd.equals("cls"))) { conIn.writeln("> " + cmd); }
					
				} catch (Exception e) { e.printStackTrace(); }
			}
			*/
		}
		
		
	}
	
	private Class checkClasses(ETerminal conIn, String firstArg, String secondArg) {
		try {
			Class first = String.class;
			Class second = String.class;
			
			if (NumberUtil.isInteger(firstArg, 10)) { first = Integer.class; }
			if (NumberUtil.isInteger(secondArg, 10)) { second = Integer.class; }
			
			if (!first.equals(second)) { return Exception.class; } //error and return if the parsed range types are not the same
			else if (first == Integer.class) { return Integer.class; } //try for integer range
			else if (first == Integer.class) { return String.class; } //try for character range instead
			
		} catch (Exception e) {
			conIn.badError("Java Error: " + e);
			e.printStackTrace();
		}
		return null;
	}
	
	private void runLoop(ETerminal conIn, Object curVal, EArrayList<String> argsIn) {
		String cmd = replaceValsInArgs(argsIn, curVal);
		conIn.writeln("> " + cmd);
		EnhancedMC.getTerminalHandler().executeCommand(conIn, cmd, false);
	}
	
	private String replaceValsInArgs(EArrayList<String> argsIn, Object curVal) {
		String cmd = "";
		for (String s : argsIn) {
			s = s.replaceAll("\\*", "" + curVal);
			cmd += s + " ";
		}
		if (argsIn.size() > 0 && cmd.length() > 0) { cmd = cmd.substring(0, cmd.length() - 1); }
		return cmd;
	}
}
