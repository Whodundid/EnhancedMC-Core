package com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables;

//Last edited: Oct 11, 2018
//First Added: Oct 11, 2018
//Author: Hunter Bragg

public class ScriptDouble extends ScriptVariable {
	
	public double val;
	
	public ScriptDouble(String name, double valIn) {
		super(name, valIn);
		val = valIn;
	}
	
	public ScriptDouble floor() { val = Math.floor(val); return this; }
	public ScriptDouble ceil() { val = Math.ceil(val); return this; }
	
}
