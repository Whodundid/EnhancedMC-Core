package com.Whodundid.core.enhancedGui.objectEvents;

import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.EventType;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

public class EventFirstDraw extends ObjectEvent {
	
	public EventFirstDraw(IEnhancedGuiObject parentObjectIn) {
		super(parentObjectIn, EventType.FirstDraw);
	}
}