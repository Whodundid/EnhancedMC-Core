package com.Whodundid.core.events.emcEvents;

import com.Whodundid.core.util.chatUtil.TimedChatLine;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ChatLineCreatedEvent extends Event {

	TimedChatLine l;
	
	public ChatLineCreatedEvent(TimedChatLine lineIn) {
		l = lineIn;
	}
	
	public TimedChatLine getLine() { return l; }
}
