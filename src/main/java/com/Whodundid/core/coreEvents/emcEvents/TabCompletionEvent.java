package com.Whodundid.core.coreEvents.emcEvents;

import com.Whodundid.core.coreEvents.EMCEvent;

//Author: Hunter Bragg
public class TabCompletionEvent extends EMCEvent {
	
	String[] completion;
	
	public TabCompletionEvent(String[] completionIn) {
		completion = completionIn;
	}
	
	public String[] getCompletion() { return completion; }
	
}
