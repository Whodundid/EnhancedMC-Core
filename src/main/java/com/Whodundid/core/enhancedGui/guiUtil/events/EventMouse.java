package com.Whodundid.core.enhancedGui.guiUtil.events;

import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.EventType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;

public class EventMouse extends ObjectEvent {
	
	MouseType type;
	int mX = 0, mY = 0;
	int button;
	
	public EventMouse(IEnhancedGuiObject parentObjectIn, int mXIn, int mYIn, int buttonIn, MouseType typeIn) {
		super(parentObjectIn, EventType.Mouse);
		type = typeIn;
	}

	public MouseType getMouseType() { return type; }
	public int getMouseX() { return mX; }
	public int getMouseY() { return mY; }
	public int getMouseButton() { return button; }
}
