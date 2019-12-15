package com.Whodundid.core.coreEvents.emcEvents;

import net.minecraftforge.fml.common.eventhandler.Event;

public class CommandNotFoundEvent extends Event {
	
	String attemptedCommand = "";
	
	public CommandNotFoundEvent(String attemptedCommandIn) {
		attemptedCommand = attemptedCommandIn;
	}
	
	public String getAttemptedCommand() { return attemptedCommand; }
}