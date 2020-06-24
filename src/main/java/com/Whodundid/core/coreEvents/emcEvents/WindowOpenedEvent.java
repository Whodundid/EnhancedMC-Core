package com.Whodundid.core.coreEvents.emcEvents;

import com.Whodundid.core.coreEvents.EMCEvent;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class WindowOpenedEvent extends EMCEvent {

	WindowParent window;
	
	public WindowOpenedEvent(WindowParent windowIn) {
		window = windowIn;
	}
	
	public WindowParent getWindow() { return window; }
	
}
