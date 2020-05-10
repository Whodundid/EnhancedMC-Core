package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.core.debug.DebugFunctions;
import com.Whodundid.core.debug.IDebugCommand;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class DebugHotKey extends HotKey {
	
	public IDebugCommand debugFunction;
	
	public DebugHotKey(String keyNameIn, KeyComboAction keysIn, IDebugCommand commandIn) { this(keyNameIn, keysIn, commandIn, false, "", null); }
	public DebugHotKey(String keyNameIn, KeyComboAction keysIn, IDebugCommand commandIn, boolean builtInVal) { this(keyNameIn, keysIn, commandIn, builtInVal, "", null); }
	public DebugHotKey(String keyNameIn, KeyComboAction keysIn, IDebugCommand commandIn, String descriptionIn) { this(keyNameIn, keysIn, commandIn, false, descriptionIn, null); }
	public DebugHotKey(String keyNameIn, KeyComboAction keysIn, IDebugCommand commandIn, boolean builtInVal, String descriptionIn, String builtInSubModTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.DEBUG, builtInSubModTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		debugFunction = commandIn;
	}
	
	public IDebugCommand getDebugFunction() { return debugFunction; }
	public DebugHotKey setDebugFunction(IDebugCommand functionIn) { debugFunction = functionIn; return this; }
	
	@Override public void executeHotKeyAction() { DebugFunctions.runDebugFunction(debugFunction); }
	
	@Override
	public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + debugFunction.getDebugCommandID());
		return base;
	}
}
