package com.Whodundid.core.coreEvents.emcEvents;

import net.minecraftforge.fml.common.eventhandler.Event;

public class GameWindowResizedEvent extends Event {
	
	private int width, height;
	
	public GameWindowResizedEvent(int newWidth, int newHeight) {
		width = newWidth;
		height = newHeight;
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
}

