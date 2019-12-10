package com.Whodundid.core.enhancedGui.objectEvents;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

//Last edited: Mar 26, 2019
//First Added: Mar 26, 2019
//Author: Hunter Bragg

public class EventAction extends ObjectEvent {
	
	IEnhancedActionObject actionObject = null;
	Object storedObject = null;
	
	public EventAction(IEnhancedGuiObject parentObjectIn, IEnhancedActionObject actionObjectIn) {
		actionObject = actionObjectIn;
		storedObject = actionObjectIn.getSelectedObject();
	}
	
	public IEnhancedActionObject getActionObject() { return actionObject; }
	public Object getStoredObject() { return storedObject; }
}
