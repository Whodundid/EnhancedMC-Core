package com.Whodundid.core.notifications;

import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

public abstract class NotificationObject extends EnhancedGuiObject {
	
	protected Notification note;
	
	protected NotificationObject(IEnhancedGuiObject parentIn, Notification noteIn) {
		note = noteIn;
		init(parentIn);
	}
	
	protected abstract void setup();
}
