package com.Whodundid.core.coreEvents.emcEvents;

import com.Whodundid.core.coreEvents.EMCEvent;

public abstract class AppsReloadedEvent extends EMCEvent {
	
	protected boolean isPre;
	
	//-------------------------
	//AppsReloadedEvent Getters
	//-------------------------
	
	public boolean isPre() { return isPre; }
	
	//--------------------------------
	//AppsReloadedEvent Static Classes
	//--------------------------------
	
	public static class Pre extends AppsReloadedEvent {
		public Pre() { super(); isPre = true; }
	}
	
	public static class Post extends AppsReloadedEvent {
		public Post() { super(); isPre = false; }
	}

}
