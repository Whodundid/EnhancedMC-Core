package com.Whodundid.core.enhancedGui.guiUtil.events;

import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.EventType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;

//Last edited: Mar 26, 2019
//First Added: Mar 26, 2019
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
