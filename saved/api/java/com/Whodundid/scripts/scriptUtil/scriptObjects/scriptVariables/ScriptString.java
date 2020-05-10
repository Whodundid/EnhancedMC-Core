package com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables;

//Last edited: Jun 18, 2019
//First Added: Jun 18, 2019
//Author: Hunter Bragg

public class ScriptString extends ScriptVariable {
	
	public String s = "";
	
	public ScriptString(String name, String in) {
		super(name, in);
		s = in;
	}
}
