package com.Whodundid.scripts.system.functions;

import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.scripts.EScript;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException;
import net.minecraft.client.Minecraft;

//Last edited: Jun 17, 2019
//First Added: Jun 17, 2019
//Author: Hunter Bragg

public interface ISystemFunction {
	
	static Minecraft mc = Minecraft.getMinecraft();
	
	public String getName();
	public void runFunction(EScript script, EArrayList<String> args) throws ScriptException;
}
