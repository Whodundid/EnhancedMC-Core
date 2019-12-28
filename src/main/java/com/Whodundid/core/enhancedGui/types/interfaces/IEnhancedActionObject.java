package com.Whodundid.core.enhancedGui.types.interfaces;

//Last edited: Aug 6, 2019
//Edit note: Added the ability to designate an actionReciever, by default it's the object it was added to.
//First Added: Dec 30, 2018
//Author: Hunter Bragg

/** An interface outlining behavior for Enhanced Gui Objects which can perform actions. */
public interface IEnhancedActionObject {
	
	//actions
	
	public void performAction(Object... args);
	public void onPress();
	public boolean runActionOnPress();
	public IEnhancedActionObject setRunActionOnPress(boolean val);
	public IEnhancedActionObject setActionReciever(IEnhancedGuiObject objIn);
	public IEnhancedGuiObject getActionReciever();
	
	//objects
	
	public IEnhancedActionObject setSelectedObject(Object objIn);
	public Object getSelectedObject();
}
