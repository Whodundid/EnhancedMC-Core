package com.Whodundid.core.windowLibrary.windowTypes.interfaces;

//Author: Hunter Bragg

/** An interface outlining behavior for IWindowObjects which can perform actions. */
public interface IActionObject extends IWindowObject {
	
	//-------
	//actions
	//-------
	
	public void performAction(Object... args);
	public void onPress();
	public boolean runsActionOnPress();
	public boolean runsActionOnRelease();
	public IActionObject setRunActionOnPress(boolean val);
	public IActionObject setRunActionOnRelease(boolean val);
	public IActionObject setActionReceiver(IWindowObject objIn);
	public IWindowObject getActionReceiver();
	
	//-------
	//objects
	//-------
	
	public IActionObject setStoredObject(Object objIn);
	public Object getStoredObject();
	public IActionObject setSelectedObject(Object objIn);
	public Object getSelectedObject();
	
	//---------------
	//Static Setters
	//---------------
	
	public static void setActionReceiver(IWindowObject receiver, IActionObject... objects) {
		for (IActionObject o : objects) { o.setActionReceiver(receiver); }
	}
	
}
