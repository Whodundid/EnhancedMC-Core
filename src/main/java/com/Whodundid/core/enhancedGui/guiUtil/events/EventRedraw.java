package com.Whodundid.core.enhancedGui.guiUtil.events;

import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.EventType;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;

public class EventRedraw extends ObjectEvent {
	
	public EventRedraw(IEnhancedGuiObject parentObjectIn) {
		super(parentObjectIn, EventType.Redraw);
	}
}
