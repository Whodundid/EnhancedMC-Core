package com.Whodundid.core.debug;

//Author: Hunter Bragg

public enum IDebugCommand {
	DEBUG_0(0),
	DEBUG_1(1),
	DEBUG_2(2),
	DEBUG_3(3),
	DEBUG_4(4);
	
	private int id = -1;
	
	IDebugCommand(int idIn) {
		id = idIn;
	}
	
	public int getDebugCommandID() { return id; }
	
	public static String getDebugCommandName(IDebugCommand commandIn) {
		switch (commandIn) {
		case DEBUG_0: return "Debug 0";
		case DEBUG_1: return "Debug 1";
		case DEBUG_2: return "Debug 2";
		case DEBUG_3: return "Debug 3";
		case DEBUG_4: return "Debug 4";
		default: return "Unknown Debug Command";
		}
	}
}
