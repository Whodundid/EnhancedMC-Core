package com.Whodundid.core.enhancedGui.objectEvents;

import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.EventType;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

public class EventRedraw extends ObjectEvent {
	
	public EventRedraw(IEnhancedGuiObject parentObjectIn) {
		super(parentObjectIn, EventType.Redraw);
	}
}
