package com.Whodundid.core.enhancedGui.guiUtil.events;

import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.EventType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.FocusType;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;

//Last edited: Dec 31, 2018
//First Added: Dec 31, 2018
//Author: Hunter Bragg

/** Event that is fired when a focus change occurs. */
public class EventFocus extends ObjectEvent {
	
	IEnhancedGuiObject eventObject;
	FocusType type;
	int actionCode = -1;
	int mX = -1, mY = -1;
	
	public EventFocus(IEnhancedGuiObject parentObjectIn, IEnhancedGuiObject eventObjectIn, FocusType typeIn) {
		super(parentObjectIn, EventType.Focus);
		parentObject = parentObjectIn;
		eventObject = eventObjectIn;
		type = typeIn;
	}
	
	public EventFocus(IEnhancedGuiObject parentObjectIn, IEnhancedGuiObject eventObjectIn, FocusType typeIn, int actionCodeIn, int mXIn, int mYIn) {
		super(parentObjectIn, EventType.Focus);
		parentObject = parentObjectIn;
		eventObject = eventObjectIn;
		type = typeIn;
		actionCode = actionCodeIn;
		mX = mXIn;
		mY = mYIn;
	}
	
	public IEnhancedGuiObject getFocusObject() { return eventObject; }
	public FocusType getFocusType() { return type; }
	public int getActionCode() { return actionCode; }
	public int getMouseX() { return mX; }
	public int getMouseY() { return mY; }
}
