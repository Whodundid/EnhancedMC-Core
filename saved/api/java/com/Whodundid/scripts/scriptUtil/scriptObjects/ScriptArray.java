package com.Whodundid.scripts.scriptUtil.scriptObjects;

import com.Whodundid.scripts.scriptUtil.ScriptKeyWordType;
import com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables.ScriptVariable;
import java.util.ArrayList;

//Last edited: Oct 11, 2018
//First Added: Oct 11, 2018
//Author: Hunter Bragg

public class ScriptArray extends ScriptObject {
	
	public String name = "";
	public ArrayList<ScriptVariable> array = new ArrayList();
	
	public ScriptArray(String nameIn) {
		name = nameIn;
		keyWordType = ScriptKeyWordType.array;
	}
}
