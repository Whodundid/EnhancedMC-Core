package com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events;

import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.ObjectEvent;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.EventType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.FocusType;

//Author: Hunter Bragg

/** Event that is fired when a focus change occurs. */
public class EventFocus extends ObjectEvent {
	
	IWindowObject eventObject;
	FocusType type;
	int actionCode = -1;
	int mX = -1, mY = -1;
	
	public EventFocus(IWindowObject parentObjectIn, IWindowObject eventObjectIn, FocusType typeIn) {
		super(parentObjectIn, EventType.Focus);
		parentObject = parentObjectIn;
		eventObject = eventObjectIn;
		type = typeIn;
	}
	
	public EventFocus(IWindowObject parentObjectIn, IWindowObject eventObjectIn, FocusType typeIn, int actionCodeIn, int mXIn, int mYIn) {
		super(parentObjectIn, EventType.Focus);
		parentObject = parentObjectIn;
		eventObject = eventObjectIn;
		type = typeIn;
		actionCode = actionCodeIn;
		mX = mXIn;
		mY = mYIn;
	}
	
	public IWindowObject getFocusObject() { return eventObject; }
	public FocusType getFocusType() { return type; }
	public int getActionCode() { return actionCode; }
	public int getMX() { return mX; }
	public int getMY() { return mY; }
	
}
