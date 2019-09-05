package com.Whodundid.core.enhancedGui.guiUtil.events;

import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.EventType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.KeyboardType;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;

public class EventKeyboard extends ObjectEvent {

	KeyboardType type;
	char eventChar;
	int eventKey;
	
	public EventKeyboard(IEnhancedGuiObject parentIn, char charIn, int keyIn, KeyboardType typeIn) {
		super(parentIn, EventType.Keyboard);
		type = typeIn;
	}
	
	public KeyboardType getKeyboardType() { return type; }
	public char getEventChar() { return eventChar; }
	public int getEventKey() { return eventKey; }
}
