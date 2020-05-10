package com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables;

//Last edited: Oct 11, 2018
//First Added: Oct 11, 2018
//Author: Hunter Bragg

public class ScriptBool extends ScriptVariable {
	
	public boolean val;
	
	public ScriptBool(String name, boolean valIn) {
		super(name, valIn);
		val = valIn;
	}
}
