package com.Whodundid.core.enhancedGui.types;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;

//Author: Hunter Bragg

public abstract class EnhancedActionObject extends EnhancedGuiObject implements IEnhancedActionObject {

	protected boolean runActionOnPress = false;
	protected Object selectedObject = null;
	protected Object storedObject = null;
	protected IEnhancedGuiObject actionReceiver;
	
	protected EnhancedActionObject() {}
	protected EnhancedActionObject(IEnhancedGuiObject parentIn) {
		actionReceiver = parentIn;
	}
	
	@Override
	public void init(IEnhancedGuiObject objIn, int xIn, int yIn) {
		super.init(objIn, xIn, yIn);
		actionReceiver = objIn;
	}
	
	@Override
	public void init(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn) {
		init(objIn, xIn, yIn, widthIn, heightIn, -1);
		actionReceiver = objIn;
	}
	
	@Override
	public void init(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn, int objectIdIn) {
		super.init(objIn, xIn, yIn, widthIn, heightIn, objectIdIn);
		actionReceiver = objIn;
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
	@Override public IEnhancedActionObject setRunActionOnPress(boolean value) { runActionOnPress = value; return this; }
	@Override public IEnhancedActionObject setActionReceiver(IEnhancedGuiObject objIn) { actionReceiver = objIn; return this; }
	@Override public IEnhancedGuiObject getActionReceiver() { return actionReceiver; }
		
	//objects
	@Override public IEnhancedActionObject setStoredObject(Object objIn) { storedObject = objIn; return this; }
	@Override public Object getStoredObject() { return storedObject; }
	@Override public IEnhancedActionObject setSelectedObject(Object objIn) { selectedObject = objIn; return this; }
	@Override public Object getSelectedObject() { return selectedObject; }
}
