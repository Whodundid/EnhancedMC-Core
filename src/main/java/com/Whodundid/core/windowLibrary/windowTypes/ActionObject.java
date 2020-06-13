package com.Whodundid.core.windowLibrary.windowTypes;

import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowParent;

//Author: Hunter Bragg

public abstract class ActionObject extends WindowObject implements IActionObject {

	protected boolean runActionOnPress = false;
	protected boolean runActionOnRelease = false;
	protected Object selectedObject = null;
	protected Object storedObject = null;
	protected IWindowObject actionReceiver;
	
	protected ActionObject() {}
	protected ActionObject(IWindowObject parentIn) {
		actionReceiver = parentIn;
	}
	
	@Override
	public void init(IWindowObject objIn, int xIn, int yIn) {
		super.init(objIn, xIn, yIn);
		actionReceiver = objIn;
	}
	
	@Override
	public void init(IWindowObject objIn, int xIn, int yIn, int widthIn, int heightIn) {
		init(objIn, xIn, yIn, widthIn, heightIn, -1);
		actionReceiver = objIn;
	}
	
	@Override
	public void init(IWindowObject objIn, int xIn, int yIn, int widthIn, int heightIn, int objectIdIn) {
		super.init(objIn, xIn, yIn, widthIn, heightIn, objectIdIn);
		actionReceiver = objIn;
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
		
		if (runActionOnRelease) { performAction(); }
	}
	
	//actions
	@Override
	public void performAction(Object... args) {
		if (actionReceiver != null) {
			IWindowParent p = actionReceiver.getWindowParent();
			if (p != null) { p.bringToFront(); }
			actionReceiver.actionPerformed(this, args);
		}
	}
	
	@Override public void onPress() {}
	@Override public boolean runsActionOnPress() { return runActionOnPress; }
	@Override public boolean runsActionOnRelease() { return runActionOnRelease; }
	@Override public IActionObject setRunActionOnPress(boolean value) { runActionOnPress = value; return this; }
	@Override public IActionObject setRunActionOnRelease(boolean val) { runActionOnRelease = val; return this; }
	@Override public IActionObject setActionReceiver(IWindowObject objIn) { actionReceiver = objIn; return this; }
	@Override public IWindowObject getActionReceiver() { return actionReceiver; }
		
	//objects
	@Override public IActionObject setStoredObject(Object objIn) { storedObject = objIn; return this; }
	@Override public Object getStoredObject() { return storedObject; }
	@Override public IActionObject setSelectedObject(Object objIn) { selectedObject = objIn; return this; }
	@Override public Object getSelectedObject() { return selectedObject; }
	
}
