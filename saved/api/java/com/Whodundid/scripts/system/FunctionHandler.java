package com.Whodundid.scripts.system;

import com.Whodundid.core.util.chatUtil.ChatBuilder;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.scripts.EScript;
import com.Whodundid.scripts.ScriptingApp;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException;
import com.Whodundid.scripts.system.functions.ISystemFunction;
import com.Whodundid.scripts.system.functions.Print;
import java.util.Iterator;
import net.minecraft.client.Minecraft;

//Last edited: Jun 18, 2019
//First Added: Jun 18, 2019
//Author: Hunter Bragg

public class FunctionHandler {
	
	Minecraft mc = Minecraft.getMinecraft();
	protected StorageBoxHolder<String, ISystemFunction> functions = new StorageBoxHolder();
	protected EArrayList<ISystemFunction> functionList = new EArrayList();
	protected EArrayList<ISystemFunction> customFunctionList = new EArrayList();
	ScriptingApp man;
	FunctionHandler instance;
	
	public FunctionHandler(ScriptingApp manIn) {
		man = manIn;
		instance = this;
		registerNativeSystemFunctions(false);
	}
	
	public ISystemFunction getFunction(String functionName) { return functions.getBoxWithObj(functionName).getValue(); }
	public EArrayList<ISystemFunction> getFunctionList() { return functionList; }
	
	public EArrayList<String> getFunctionNames() {
		EArrayList<String> returnList = new EArrayList();
		for (StorageBox<String, ISystemFunction> getBox : functions) {
			returnList.add(getBox.getObject());
		}
		return returnList;
	}
	
	private void registerNativeSystemFunctions(boolean runVisually) {
		registerFunction(new Print(), runVisually);
	}
	
	public void registerFunction(ISystemFunction functionIn, boolean runVisually) {
		functionList.add(functionIn);
		if (mc.thePlayer != null) { mc.thePlayer.addChatMessage(ChatBuilder.of("Registering function: " + functionIn.getName()).build()); }
		functions.put(functionIn.getName(), functionIn);
		if (mc.thePlayer != null) { mc.thePlayer.addChatMessage(ChatBuilder.of("Registering function call: " + functionIn.getName()).build()); }
	}
	
	public void executeFunction(EScript script, String token, EArrayList<String> args) throws ScriptException {
		token = token.trim().toLowerCase();
		if (functions.getBoxWithObj(token) != null) {
			ISystemFunction function = functions.getBoxWithObj(token).getValue();
			
			if (function == null) {
				System.out.println("function read error!");
				return;
			}
			
			function.runFunction(script, args);
		}
	}
	
	public synchronized void reregisterAllFunctions(boolean runVisually) {
		Iterator<ISystemFunction> a = functionList.iterator();
		while (a.hasNext()) {
			String functionName = a.next().getName();
			if (runVisually && mc.thePlayer != null) { mc.thePlayer.addChatMessage(ChatBuilder.of("Unregistering system function: " + functionName).build()); }
			a.remove();
		}
		Iterator<StorageBox<String, ISystemFunction>> b = functions.iterator();
		while (b.hasNext()) {
			String functionName = b.next().getObject();
			if (runVisually && mc.thePlayer != null) { mc.thePlayer.addChatMessage(ChatBuilder.of("Unregistering system function call: " + functionName).build()); }
		}
		registerNativeSystemFunctions(runVisually);
		for (ISystemFunction function : customFunctionList) {
			registerFunction(function, runVisually);
		}
	}
}
