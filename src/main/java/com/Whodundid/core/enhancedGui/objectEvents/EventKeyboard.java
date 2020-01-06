package com.Whodundid.core.enhancedGui.objectEvents;

import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.EventType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.KeyboardType;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

//Author: Hunter Bragg

public class EventKeyboard extends ObjectEvent {

	KeyboardType type;
	char eventChar;
	int eventKey;
	
	public EventKeyboard(IEnhancedGuiObject parentIn, char charIn, int keyIn, KeyboardType typeIn) {
		super(parentIn, EventType.Keyboard);
		eventChar = charIn;
		eventKey = keyIn;
		type = typeIn;
	}
	
	public KeyboardType getKeyboardType() { return type; }
	public char getEventChar() { return eventChar; }
	public int getEventKey() { return eventKey; }
}
