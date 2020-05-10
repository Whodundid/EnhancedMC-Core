package com.Whodundid.scripts.scriptUtil.scriptObjects;

import com.Whodundid.scripts.scriptUtil.ScriptKeyWordType;

//Last edited: Oct 11, 2018
//First Added: Oct 11, 2018
//Author: Hunter Bragg

public abstract class ScriptObject {
	
	protected ScriptKeyWordType keyWordType = ScriptKeyWordType.undefined;
	
	protected ScriptObject() {}
	
	public ScriptKeyWordType getKeyWordType() { return keyWordType; }
}
