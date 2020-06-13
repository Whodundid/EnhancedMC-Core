package com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events;

import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.ObjectEvent;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventFirstDraw extends ObjectEvent {
	
	public EventFirstDraw(IWindowObject parentObjectIn) {
		super(parentObjectIn, EventType.FirstDraw);
	}
	
}
