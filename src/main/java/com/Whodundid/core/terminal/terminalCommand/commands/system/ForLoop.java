package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.mathUtil.NumberUtil;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class ForLoop extends TerminalCommand {
	
	public ForLoop() {
		super(CommandType.NORMAL);
		numArgs = 2;
	}

	@Override public String getName() { return "for"; }
	@Override public boolean showInHelp() { return EnhancedMC.isOpMode(); }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Runs a command n number of times in given range replacing any '#' arguments with current value."; }
	@Override public String getUsage() { return "ex: for 0-9 server ping 192.168.0.#"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() < 2) { termIn.error("Not enought arguments for loop!"); termIn.info(getUsage()); }
		else if (args.size() >= 2) {
			String vals = args.get(0);
			
			EArrayList<String> otherArgs = new EArrayList(args.subList(1, args.size()));
			
			//String cmd = "";
			//for (String s : otherArgs) { cmd += (s + " "); }
			//if (otherArgs.size() >= 1) { cmd = cmd.substring(0, cmd.length() - 1); }
			
			if (vals.length() < 3) { termIn.error("Not enough arguments for loop!"); }
			else if (vals.length() >= 3 && vals.contains("-")) {
				int pos = EUtil.findStartingIndex(vals, "-");
				String firstArg = vals.substring(0, pos);
				String secondArg = vals.substring(pos + 1);
				
				boolean positive = true;
				int firstI = 0;
				int secondI = 0;
				
				Class type = checkClasses(termIn, firstArg, secondArg);
				
				if (type == null) { termIn.error("Could not parse range value types!"); return; }
				if (type == Exception.class) { termIn.error("Inconsistent range datatype values!"); return; }
				
				if (type == Integer.class) {
					firstI = Integer.parseInt(firstArg);
					secondI = Integer.parseInt(secondArg);
					positive = (secondI - firstI > 0); //check direction
					
					if (positive) {
						try {
							for (int i = firstI; i <= secondI; i++) {
								runLoop(termIn, i, otherArgs);
							}
						} catch (Exception e) {
							termIn.badError("Java Error: " + e);
							e.printStackTrace();
						}
					}
					else {
						try {
							for (int i = firstI; i >= secondI; i--) {
								runLoop(termIn, i, otherArgs);
							}
						} catch (Exception e) {
							termIn.badError("Java Error: " + e);
							e.printStackTrace();
						}
					}
				}
				else if (type == String.class) {
					
				}
			}
			else {
				termIn.error("Invalid for loop range argument!");
			}
			
			/*
			if (!cmd.isEmpty()) {
				try {
					if (!(cmd.equals("clear") || cmd.equals("clr") || cmd.equals("cls"))) { termIn.writeln("> " + cmd); }
					
				} catch (Exception e) { e.printStackTrace(); }
			}
			*/
		}
		
		
	}
	
	private Class checkClasses(ETerminal termIn, String firstArg, String secondArg) {
		try {
			Class first = String.class;
			Class second = String.class;
			
			if (NumberUtil.isInteger(firstArg, 10)) { first = Integer.class; }
			if (NumberUtil.isInteger(secondArg, 10)) { second = Integer.class; }
			
			if (!first.equals(second)) { return Exception.class; } //error and return if the parsed range types are not the same
			else if (first == Integer.class) { return Integer.class; } //try for integer range
			else if (first == Integer.class) { return String.class; } //try for character range instead
			
		} catch (Exception e) {
			termIn.badError("Java Error: " + e);
			e.printStackTrace();
		}
		return null;
	}
	
	private void runLoop(ETerminal termIn, Object curVal, EArrayList<String> argsIn) {
		String cmd = replaceValsInArgs(argsIn, curVal);
		termIn.writeln("> " + cmd);
		EnhancedMC.getTerminalHandler().executeCommand(termIn, cmd, false);
	}
	
	private String replaceValsInArgs(EArrayList<String> argsIn, Object curVal) {
		String cmd = "";
		for (String s : argsIn) {
			s = s.replaceAll("\\#", "" + curVal);
			cmd += s + " ";
		}
		if (argsIn.size() > 0 && cmd.length() > 0) { cmd = cmd.substring(0, cmd.length() - 1); }
		return cmd;
	}
}
