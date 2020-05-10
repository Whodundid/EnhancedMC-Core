package com.Whodundid.scripts;

import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.scripts.scriptUtil.ScriptReader;
import com.Whodundid.scripts.scriptUtil.ScriptStates;
import com.Whodundid.scripts.scriptUtil.scriptObjects.ScriptArray;
import com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables.ScriptVarType;
import com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables.ScriptVariable;
import java.util.concurrent.TimeUnit;

//Last edited: Jun 17, 2019
//First Added: Jun 17, 2019
//Author: Hunter Bragg

public class EScript {
	
	private String name = "no script name";
	private EArrayList<String> lines = null;
	private EArrayList<String> args = new EArrayList();
	private EArrayList<ScriptVariable> vars = new EArrayList();
	private EArrayList<ScriptArray> arrays = new EArrayList();
	private int scriptID = 0;
	private int size;
	private long startTime = 0l;
	private long sleepStart = 0l;
	private long sleepDuration = 0l;
	private boolean showStageMessages = false;
	private ScriptStates state = ScriptStates.dead;
	private ScriptReader reader;
	
	public EScript(String nameIn, EArrayList<String> linesIn) {
		name = nameIn;
		lines = linesIn;
		size = linesIn.size();
		reader = new ScriptReader(this);
	}
	
	public void setArgs(EArrayList<String> argsIn) {
		if (args != null) { args = argsIn; }
		else { args = new EArrayList<String>(); }
	}
	
	public String getLineAt(int lineNumIn) {
		if (lineNumIn < 0 || lineNumIn > size) { return null; }
		return lines.get(lineNumIn);
	}
	
	public void setSleep(long duration) {
		sleepStart = System.currentTimeMillis();
		sleepDuration = duration;
	}
	
	public ScriptVarType checkForType(String tokenIn) {
		if (tokenIn == null) { return ScriptVarType.UNDEFINED; }
		for (ScriptVariable v : getVars()) {
			if (v.getName().equals(tokenIn)) { return v.getVariableType(); }
		}
		return ScriptVarType.UNDEFINED;
	}
	
	/** Check if token is a variable name. */
	public boolean isVariable(String tokenIn) {
		if (tokenIn == null) { return false; }
		for (ScriptVariable v : getVars()) {
			if (v.getName().equals(tokenIn)) { return true; }
		}
		return false;
	}
	
	public ScriptVariable getVariable(String tokenIn) {
		if (tokenIn == null) { return null; }
		for (ScriptVariable v : getVars()) {
			if (v.getName().equals(tokenIn)) { return v; }
		}
		return null;
	}
	
	public void setStartTime(long timeIn) { startTime = timeIn; }
	public void setID(int idIn) { scriptID = idIn; }
	public int getScriptID() { return scriptID; }
	public String getScriptName() { return name; }
	public int getLength() { return size; }
	public EArrayList<String> getArgs() { return args; }
	public EArrayList<ScriptVariable> getVars() { return vars; }
	public EArrayList<ScriptArray> getArrays() { return arrays; }
	public boolean isRunning() { return state != ScriptStates.dead; }
	public ScriptStates getState() { return state; }
	public void setState(ScriptStates stateIn) { state = stateIn; }
	public long getSleepStart() { return sleepStart; }
	public long getSleepDuration() { return sleepDuration; }
	public long getAliveTime() { return isRunning() ? TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) : 0l; }
	public void clearSleep() { sleepStart = 0l; sleepDuration = 0l; }
	public ScriptReader getReader() { return reader; }
}
