package com.Whodundid.core.coreEvents.emcEvents;

import com.Whodundid.core.coreEvents.EMCEvent;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;

public class WindowClosedEvent extends EMCEvent {

	WindowParent window;
	
	public WindowClosedEvent(WindowParent windowIn) {
		window = windowIn;
	}
	
	public WindowParent getWindow() { return window; }
	
}
