package com.Whodundid.core.notifications;

import com.Whodundid.core.EnhancedMC;
import java.util.ArrayDeque;
import java.util.Deque;

public class NotificationHandler {
	
	protected static Deque<Notification> notificationQueue = new ArrayDeque();
	protected static Notification curNot = null;
	protected static long notDuration = 3000l;
	protected static long startTime = 0l;
	
	public static void post(Notification n) { if (n != null) { notificationQueue.add(n); } }
	public static void clearNotifications() { notificationQueue.clear(); }
	
	protected static void updateNotifications() {
		if (curNot != null) {
			if (System.currentTimeMillis() - startTime > notDuration) {
				
			}
		}
	}
	
	protected static void displayNextNotification() {
		if (!notificationQueue.isEmpty()) {
			Notification n = notificationQueue.pop();
			if (n != null) {
				
			}
		}
	}
	
	protected static void removeCurrentNotification() {
		//EnhancedMC.getRenderer().removeObject(curNot.nObject);
	}
}
