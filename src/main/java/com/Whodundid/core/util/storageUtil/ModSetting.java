package com.Whodundid.core.util.storageUtil;

import java.util.Collection;

//Author: Hunter Bragg

public class ModSetting<Val> {
	
	private Val val = null;
	private EArrayList<Val> additionalArgs = new EArrayList();
	
	public ModSetting(Val initialValue) {
		val = initialValue;
	}
	
	public ModSetting set(Val valIn) { val = valIn; return this; }
	public ModSetting setArgs(Val... argsIn) { additionalArgs.addA(argsIn); return this; }
	public ModSetting setArgs(Collection<Val> argsIn) { additionalArgs.addAll(argsIn); return this; }
	
	public Val get() { return val; }
	public EArrayList<Val> getArgs() { return additionalArgs; }
}
