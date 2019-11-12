package com.Whodundid.core.events.emcEvents;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ModCalloutEvent extends Event {
	
	Object sender, receiver;
	String senderMessage = "", receiverMessage = "";
	Object senderObject, receiverObject;
	int mX = 0, mY = 0;
	
	public ModCalloutEvent(Object objIn) { this(objIn, "", -1, -1); }
	public ModCalloutEvent(Object objIn, String senderMessageIn) { this(objIn, senderMessageIn, -1, -1); }
	public ModCalloutEvent(Object objIn, String senderMessageIn, int mXIn, int mYIn) { this(objIn, senderMessageIn, null, -1, -1); }
	public ModCalloutEvent(Object objIn, String senderMessageIn, Object senderObjectIn, int mXIn, int mYIn) {
		sender = objIn;
		senderMessage = senderMessageIn;
		senderObject = senderObjectIn;
		mX = mXIn;
		mY = mYIn;
	}
	
	public ModCalloutEvent respond(Object objectIn) { return respond(objectIn, "", null); }
	public ModCalloutEvent respond(Object objectIn, String msgIn) { return respond(objectIn, msgIn, null); }
	public ModCalloutEvent respond(Object objectIn, String msgIn, Object receiverObjIn) {
		receiver = objectIn;
		receiverMessage = msgIn;
		receiverObject = receiverObjIn;
		return this;
	}
	
	public boolean checkSenderMsg(String testIn) { return senderMessage.equals(testIn); }
	public boolean checkReceiverMsg(String testIn) { return receiverMessage.equals(testIn); }
	
	public Object getSender() { return sender; }
	public String getSenderMessage() { return senderMessage; }
	public String getRecieverMessage() { return receiverMessage; }
	public Object getSenderObject() { return senderObject; }
	public Object getReceiverObject() { return receiverObject; }
	
	public int getMX() { return mX; }
	public int getMY() { return mY; }
}
