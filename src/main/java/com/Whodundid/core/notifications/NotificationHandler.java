package com.Whodundid.core.notifications;

import java.util.ArrayDeque;
import java.util.Deque;

public class NotificationHandler {
	
	protected static Deque<Notification> focusQueue = new ArrayDeque();
	protected static Notification curNot = null;
	
	public static void post(Notification n) { if (n != null) { focusQueue.add(n); } }
	public static void clearNotifications() { focusQueue.clear(); }
	
	public static void displayNext() { if (!focusQueue.isEmpty()) { displayNotification(focusQueue.pop()); } }
	
	protected static void displayNotification(Notification n) {
		
	}
}
