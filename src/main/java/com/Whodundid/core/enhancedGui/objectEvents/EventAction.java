package com.Whodundid.core.enhancedGui.objectEvents;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

//Last edited: Mar 26, 2019
//First Added: Mar 26, 2019
//Author: Hunter Bragg

public class EventAction extends ObjectEvent {
	
	IEnhancedActionObject actionObject = null;
	Object storedObject = null;
	Object[] args = null;
	
	public EventAction(IEnhancedGuiObject parentObjectIn, IEnhancedActionObject actionObjectIn, Object[] argsIn) {
		actionObject = actionObjectIn;
		storedObject = actionObjectIn.getSelectedObject();
		args = argsIn;
	}
	
	public IEnhancedActionObject getActionObject() { return actionObject; }
	public Object getStoredObject() { return storedObject; }
	public Object[] getArgs() { return args; }
}
