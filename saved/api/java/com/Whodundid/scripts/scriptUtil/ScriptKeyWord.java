package com.Whodundid.scripts.scriptUtil;

public enum ScriptKeyWord {
	Script,
	Int,
	DInt,
	Bool,
	String,
	For,
	Do,
	While,
	Jump,
	System,
	Player,
	World,
	Server,
	Undefined;
	
	public static boolean isStringKeyWord(String in) {
		try {
			valueOf(in);
			return true;
		} catch (IllegalArgumentException e) {}
		
		switch (in) {
		case "Script":
		case "int":
		case "dint":
		case "bool":
		case "string":
		case "for":
		case "do":
		case "while":
		case "jump":
		case "System":
		case "Player":
		case "World":
		case "Server": return true;
		default: return false;
		}
	}
	
	public static ScriptKeyWord getKeyWordFromString(String in) {
		try { return valueOf(in); } catch (IllegalArgumentException e) {}
		
		switch (in) {
		case "Script": return Script;
		case "int": return Int;
		case "dint": return DInt;
		case "bool": return Bool;
		case "string": return String;
		case "for": return For;
		case "do": return Do;
		case "while": return While;
		case "jump": return Jump;
		case "System": return System;
		case "Player": return Player;
		case "World": return World;
		case "Server": return Server;
		default: return Undefined;
		}
	}
}
