package com.Whodundid.core.enhancedGui.types.interfaces;

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
	
	public IEnhancedActionObject setStorredObject(Object objIn);
	public Object getStorredObject();
	public IEnhancedActionObject setSelectedObject(Object objIn);
	public Object getSelectedObject();
}
