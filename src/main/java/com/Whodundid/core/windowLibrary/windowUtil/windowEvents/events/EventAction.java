package com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events;

import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.ObjectEvent;

//Author: Hunter Bragg

public class EventAction extends ObjectEvent {
	
	IActionObject actionObject = null;
	Object storedObject = null;
	Object[] args = null;
	
	public EventAction(IWindowObject parentObjectIn, IActionObject actionObjectIn, Object[] argsIn) {
		actionObject = actionObjectIn;
		storedObject = actionObjectIn.getSelectedObject();
		args = argsIn;
	}
	
	public IActionObject getActionObject() { return actionObject; }
	public Object getStoredObject() { return storedObject; }
	public Object[] getArgs() { return args; }
	
}
