package com.Whodundid.core.coreEvents.emcEvents;

import com.Whodundid.core.coreEvents.EMCEvent;

public class GameWindowResizedEvent extends EMCEvent {
	
	private int width, height;
	
	public GameWindowResizedEvent(int newWidth, int newHeight) {
		width = newWidth;
		height = newHeight;
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
}

