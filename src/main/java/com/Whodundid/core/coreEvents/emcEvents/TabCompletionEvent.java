package com.Whodundid.core.coreEvents.emcEvents;

import net.minecraftforge.fml.common.eventhandler.Event;

//Author: Hunter Bragg
public class TabCompletionEvent extends Event {
	
	String[] completion;
	
	public TabCompletionEvent(String[] completionIn) {
		completion = completionIn;
	}
	
	public String[] getCompletion() { return completion; }
	
}
