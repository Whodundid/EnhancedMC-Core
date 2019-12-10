package com.Whodundid.core.notifications.baseObjects;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.notifications.Notification;
import com.Whodundid.core.notifications.NotificationObject;

public class ChatNotification extends NotificationObject {
	
	protected ChatNotification(Notification noteIn) {
		super(EnhancedMC.getRenderer(), noteIn);
	}

	@Override
	protected void setup() {
		if (note != null) {
			
		}
	}

}
