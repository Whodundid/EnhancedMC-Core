package com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events;

import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.ObjectEvent;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventRedraw extends ObjectEvent {
	
	public EventRedraw(IWindowObject parentObjectIn) {
		super(parentObjectIn, EventType.Redraw);
	}
	
}
