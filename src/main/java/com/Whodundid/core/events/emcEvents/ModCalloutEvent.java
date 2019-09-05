package com.Whodundid.core.events.emcEvents;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ModCalloutEvent extends Event {
	
	Object eventObject;
	boolean hasResponded = false;
	String response = "";
	String callerMessage = "";
	Object responseObject;
	int mX = 0;
	int mY = 0;
	
	public ModCalloutEvent(Object objIn) { this(objIn, "", -1, -1); }
	public ModCalloutEvent(Object objIn, String callerMessageIn) { this(objIn, callerMessageIn, -1, -1); }
	public ModCalloutEvent(Object objIn, String callerMessageIn, int mXIn, int mYIn) {
		eventObject = objIn;
		callerMessage = callerMessageIn;
		mX = mXIn;
		mY = mYIn;
	}
	
	public Object getCaller() { return eventObject; }
	public ModCalloutEvent respond(String responseIn) { response = responseIn; hasResponded = true; return this; }
	public ModCalloutEvent setResponseObject(Object objectIn) { responseObject = objectIn; return this; }
	
	public boolean check() { return hasResponded; }
	public String getResponse() { return response; }
	public String getCallerMessage() { return callerMessage; }
	public Object getResponseObject() { return responseObject; }
	public int getMX() { return mX; }
	public int getMY() { return mY; }
}
