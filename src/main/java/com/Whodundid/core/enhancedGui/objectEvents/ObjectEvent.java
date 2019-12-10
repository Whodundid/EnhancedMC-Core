package com.Whodundid.core.enhancedGui.objectEvents;

import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.EventType;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

//Last edited: Mar 26, 2019
//First Added: Mar 26, 2019
//Author: Hunter Bragg

/** The base event class pertaining to all IEnhancedGuiObject events. */
public class ObjectEvent {
	
	IEnhancedGuiObject parentObject = null;
	EventType eventType = null;
	
	protected ObjectEvent() {}
	
	public ObjectEvent(IEnhancedGuiObject parentObjectIn, EventType typeIn) {
		parentObject = parentObjectIn;
		eventType = typeIn;
	}
	
	/** Returns the parent object from which the event was created. */
	public IEnhancedGuiObject getEventParent() { return parentObject; }
	
	/** Returns the type of event this is */
	public EventType getEventType() { return eventType; }
}
