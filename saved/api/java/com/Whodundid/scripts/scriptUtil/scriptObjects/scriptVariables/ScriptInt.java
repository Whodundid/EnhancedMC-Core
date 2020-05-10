package com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables;

//Last edited: Oct 11, 2018
//First Added: Oct 11, 2018
//Author: Hunter Bragg

public class ScriptInt extends ScriptVariable {
	
	public int val;
	
	public ScriptInt(String name, int valIn) {
		super(name, valIn);
		val = valIn;
	}
	
	public ScriptInt increment() { val++; return this; }
	public ScriptInt decrement() { val--; return this; }
}
