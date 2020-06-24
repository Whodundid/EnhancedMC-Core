package com.Whodundid.core.coreEvents.emcEvents;

import com.Whodundid.core.coreEvents.EMCEvent;

//Author: Hunter Bragg

public class CommandNotFoundEvent extends EMCEvent {
	
	String attemptedCommand = "";
	
	public CommandNotFoundEvent(String attemptedCommandIn) {
		attemptedCommand = attemptedCommandIn;
	}
	
	public String getAttemptedCommand() { return attemptedCommand; }
	
}
