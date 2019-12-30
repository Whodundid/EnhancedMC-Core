package com.Whodundid.core.notifications;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.notifications.baseObjects.EMCNotification;
import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.client.Minecraft;

public class NotificationHandler {
	
	private static NotificationHandler instance = null;
	protected Deque<NotificationObject> notificationQueue;
	protected NotificationObject curNote = null;
	protected long delayStart = 0l;
	protected long delayTime = 300l;
	
	public static NotificationHandler getHandler() {
		return instance = instance != null ? instance : new NotificationHandler();
	}

	private NotificationHandler() {
		notificationQueue = new ArrayDeque();
	}

	public NotificationHandler post(String message, WindowParent attentionWindow) { return post(new EMCNotification(message)); }
	public NotificationHandler post(NotificationObject obj) {
		if (obj != null) { notificationQueue.add(obj); }
		return this;
	}
	
	public void update() {
		if (curNote == null && !notificationQueue.isEmpty()) {
			if (System.currentTimeMillis() - delayStart > delayTime) {
				if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().ingameGUI != null) {
					displayNextNotification();
				}
			}
		}
	}
	
	public void clearNotifications() {
		if (EnhancedMC.isEGuiOpen(NotificationObject.class)) {
			Object o = EnhancedMC.getWindowInstance(NotificationObject.class);
			if (o instanceof NotificationObject) {
				NotificationObject obj = (NotificationObject) o;
				obj.close();
			}
		}
		notificationQueue.clear();
		curNote = null;
	}

	protected void displayNextNotification() {
		if (!notificationQueue.isEmpty()) {
			NotificationObject n = notificationQueue.pop();
			curNote = n;
			EnhancedMC.getRenderer().addObject(curNote);
		}
	}
	
	public void removeCurrentNotification() {
		curNote = null;
		delayStart = System.currentTimeMillis();
	}
}