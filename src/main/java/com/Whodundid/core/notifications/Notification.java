package com.Whodundid.core.notifications;

public class Notification {
	
	NotificationType type;
	String message = "";
	Object nObject = null;
	
	public Notification(NotificationType typeIn, String messageIn) { this(typeIn, messageIn, null); }
	public Notification(NotificationType typeIn, String messageIn, Object objectIn) {
		type = typeIn;
		message = messageIn;
		nObject = objectIn;
	}
	
	public NotificationType getType() { return type; }
	public String getMessage() { return message; }
	public Object getObject() { return nObject; }

}
