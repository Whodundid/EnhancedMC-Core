package com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables;

import com.Whodundid.scripts.scriptUtil.scriptObjects.ScriptObject;

public abstract class ScriptVariable extends ScriptObject {
	
	protected final String varName;
	protected final ScriptVarType variableType;
	
	public ScriptVariable(String name, Object objIn) {
		varName = name;
		variableType = checkType(objIn);
	}
	
	public String getName() { return varName; }
	public ScriptVarType getVariableType() { return variableType; }
	
	public static ScriptVarType checkType(Object objIn) {
		if (objIn instanceof Integer) { return ScriptVarType.INT; }
		else if (objIn instanceof Double) { return ScriptVarType.DOUBLE; }
		else if (objIn instanceof Boolean) { return ScriptVarType.BOOL; }
		else if (objIn instanceof String) { return ScriptVarType.STRING; }
		else { return ScriptVarType.UNDEFINED; }
	}
}
