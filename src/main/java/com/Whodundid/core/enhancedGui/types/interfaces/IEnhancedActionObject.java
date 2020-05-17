package com.Whodundid.core.enhancedGui.types.interfaces;

//Author: Hunter Bragg

/** An interface outlining behavior for Enhanced Gui Objects which can perform actions. */
public interface IEnhancedActionObject {
	
	//actions
	
	public void performAction(Object... args);
	public void onPress();
	public boolean runsActionOnPress();
	public IEnhancedActionObject setRunActionOnPress(boolean val);
	public IEnhancedActionObject setActionReceiver(IEnhancedGuiObject objIn);
	public IEnhancedGuiObject getActionReceiver();
	
	//objects
	
	public IEnhancedActionObject setStoredObject(Object objIn);
	public Object getStoredObject();
	public IEnhancedActionObject setSelectedObject(Object objIn);
	public Object getSelectedObject();
}
