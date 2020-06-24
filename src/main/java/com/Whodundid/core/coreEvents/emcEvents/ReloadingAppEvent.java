package com.Whodundid.core.coreEvents.emcEvents;

import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.coreEvents.EMCEvent;

public abstract class ReloadingAppEvent extends EMCEvent {
	
	private EMCApp app;
	protected boolean isPre;
	
	//------------------------------
	//ReloadingAppEvent Constructors
	//------------------------------
	
	protected ReloadingAppEvent(EMCApp appIn) { app = appIn; }
	
	//-------------------------
	//ReloadingAppEvent Getters
	//-------------------------
	
	public EMCApp getApp() { return app; }
	public boolean isPre() { return isPre; }
	
	//--------------------------------
	//ReloadingAppEvent Static Classes
	//--------------------------------
	
	public static class Pre extends ReloadingAppEvent {
		public Pre(EMCApp appIn) { super(appIn); isPre = true; }
	}
	
	public static class Post extends ReloadingAppEvent {
		public Post(EMCApp appIn) { super(appIn); isPre = false; }
	}

}
