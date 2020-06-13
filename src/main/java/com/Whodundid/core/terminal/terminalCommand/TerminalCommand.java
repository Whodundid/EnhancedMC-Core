package com.Whodundid.core.terminal.terminalCommand;

import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.util.MathHelper;

//Author: Hunter Bragg

public abstract class TerminalCommand {
	
	private CommandType type = CommandType.NORMAL;
	private String category = "none";
	protected EArrayList<String> modifiers = new EArrayList();
	protected int numArgs = 0;
	
	//----------------------------
	//TerminalCommand Constructors
	//----------------------------
	
	public TerminalCommand(CommandType typeIn) {
		type = typeIn;
		Integer i = new Integer(0);
	}
	
	//--------------------------------
	//TerminalCommand Abstract Methods
	//--------------------------------
	
	public abstract String getName();
	public abstract boolean showInHelp();
	public abstract EArrayList<String> getAliases();
	public abstract String getHelpInfo(boolean runVisually);
	public abstract String getUsage();
	public abstract void handleTabComplete(ETerminal conIn, EArrayList<String> args);
	public abstract void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually);
	
	//-----------------------
	//TerminalCommand Getters
	//-----------------------
	
	public CommandType getType() { return type; }
	public EArrayList<String> getModifiers() { return modifiers; }
	public String getCategory() { return category; }
	public int getNumArgs() { return numArgs; }
	
	//-----------------------
	//TerminalCommand Setters
	//-----------------------
	
	public TerminalCommand setModifiers(String... in) { modifiers = new EArrayList().addA(in); return this; }
	public TerminalCommand setCategory(String in) { category = in; return this; }
	
	//---------------------------------
	//TerminalCommand Protected Methods
	//---------------------------------
	
	public void onConfirmation(String response) {}
	
	protected boolean checkForModifier(String in) {
		return in != null && in.length() >= 1 && in.startsWith("-") && modifiers.contains(in);
	}
	
	protected void basicTabComplete(ETerminal termIn, EArrayList<String> args, EArrayList<String> completionsIn) {
		int limit = numArgs == -1 ? args.size() : MathHelper.clamp_int(args.size(), 0, numArgs);
		
		if (args.isEmpty()) {
			termIn.buildTabCompletions(completionsIn);
		}
		else if (args.size() == limit) {
			if (!termIn.getTab1()) {
				int arg = termIn.getCurrentArg() - 1;
				try {
					String input = args.get(arg);
				
					EArrayList<String> options = new EArrayList();
				
					for (String s : completionsIn) {
						if (s.startsWith(input)) { options.add(s); }
					}
				
					termIn.buildTabCompletions(options);
				}
				catch (IndexOutOfBoundsException e) {}
				catch (Exception e) { e.printStackTrace(); }
			}
		}
		
	}
	
	protected void error(ETerminal termIn, Throwable e) {
		StackTraceElement[] trace = e.getStackTrace();
		String errLoc = (trace != null && trace[0] != null) ? "\n" + trace[0].toString() : null;
		termIn.javaError(e.toString() + errLoc);
		e.printStackTrace();
	}
	
}
