package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.core.debug.DebugFunctions;
import com.Whodundid.core.debug.IDebugCommand;
import com.Whodundid.hotkeys.control.Hotkey;
import com.Whodundid.hotkeys.control.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class DebugHotkey extends Hotkey {
	
	public IDebugCommand debugFunction;
	
	public DebugHotkey(String keyNameIn, KeyComboAction keysIn, IDebugCommand commandIn) { this(keyNameIn, keysIn, commandIn, false, "", null); }
	public DebugHotkey(String keyNameIn, KeyComboAction keysIn, IDebugCommand commandIn, boolean builtInVal) { this(keyNameIn, keysIn, commandIn, builtInVal, "", null); }
	public DebugHotkey(String keyNameIn, KeyComboAction keysIn, IDebugCommand commandIn, String descriptionIn) { this(keyNameIn, keysIn, commandIn, false, descriptionIn, null); }
	public DebugHotkey(String keyNameIn, KeyComboAction keysIn, IDebugCommand commandIn, boolean builtInVal, String descriptionIn, String builtInAppTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.DEBUG, builtInAppTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		debugFunction = commandIn;
	}
	
	public IDebugCommand getDebugFunction() { return debugFunction; }
	public DebugHotkey setDebugFunction(IDebugCommand functionIn) { debugFunction = functionIn; return this; }
	
	@Override public void executeHotKeyAction() { DebugFunctions.runDebugFunction(debugFunction); }
	
	@Override
	public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + debugFunction.getDebugCommandID());
		return base;
	}
}
