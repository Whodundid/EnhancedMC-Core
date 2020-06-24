package com.Whodundid.core.coreEvents.emcEvents;

import com.Whodundid.core.coreEvents.EMCEvent;
import com.Whodundid.core.util.chatUtil.TimedChatLine;

//Author: Hunter Bragg

public class ChatLineCreatedEvent extends EMCEvent {

	TimedChatLine l;
	
	public ChatLineCreatedEvent(TimedChatLine lineIn) {
		l = lineIn;
	}
	
	public TimedChatLine getLine() { return l; }
	
}
