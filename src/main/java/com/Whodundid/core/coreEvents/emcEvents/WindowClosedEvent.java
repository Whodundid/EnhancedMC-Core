package com.Whodundid.core.coreEvents.emcEvents;

import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class WindowClosedEvent extends Event {

	WindowParent window;
	
	public WindowClosedEvent(WindowParent windowIn) {
		window = windowIn;
	}
	
	public WindowParent getWindow() { return window; }
	
}
