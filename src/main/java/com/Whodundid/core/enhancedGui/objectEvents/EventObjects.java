package com.Whodundid.core.enhancedGui.objectEvents;

import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.EventType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectEventType;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

public class EventObjects extends ObjectEvent {
	
	ObjectEventType type;
	IEnhancedGuiObject targetObject;
	
	public EventObjects(IEnhancedGuiObject parentIn, IEnhancedGuiObject targetObjectIn, ObjectEventType typeIn) {
		super(parentIn, EventType.Object);
		targetObject = targetObjectIn;
		type = typeIn;
	}
	
	public ObjectEventType getObjectEventType() { return type; }
	public IEnhancedGuiObject getTargetObject() { return targetObject; }
}
