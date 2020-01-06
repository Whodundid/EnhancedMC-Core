package com.Whodundid.core.enhancedGui.objectEvents;

import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.EventType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

//Author: Hunter Bragg

public class EventModify extends ObjectEvent {
	
	IEnhancedGuiObject modifyingObject = null;
	ObjectModifyType modifyType = ObjectModifyType.None;
	
	public EventModify(IEnhancedGuiObject parentObjectIn, IEnhancedGuiObject modifyingObjectIn, ObjectModifyType modifyingTypeIn) {
		super(parentObjectIn, EventType.Modify);
		parentObject = parentObjectIn;
		modifyingObject = modifyingObjectIn;
		modifyType = modifyingTypeIn;
	}
	
	public IEnhancedGuiObject getModifyingObect() { return modifyingObject; }
	public ObjectModifyType getModifyType() { return modifyType; }
}
