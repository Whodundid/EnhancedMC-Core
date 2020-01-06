package com.Whodundid.core.enhancedGui.types;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;

//Author: Hunter Bragg

public abstract class EnhancedActionObject extends EnhancedGuiObject implements IEnhancedActionObject {

	protected boolean runActionOnPress = false;
	protected Object storedObject = null;
	protected IEnhancedGuiObject actionReciever;
	
	protected EnhancedActionObject() {}
	protected EnhancedActionObject(IEnhancedGuiObject parentIn) {
		actionReciever = parentIn;
	}
	
	@Override
	public void init(IEnhancedGuiObject objIn, int xIn, int yIn) {
		super.init(objIn, xIn, yIn);
		actionReciever = objIn;
	}
	
	@Override
	public void init(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn) {
		init(objIn, xIn, yIn, widthIn, heightIn, -1);
		actionReciever = objIn;
	}
	
	@Override
	public void init(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn, int objectIdIn) {
		super.init(objIn, xIn, yIn, widthIn, heightIn, objectIdIn);
		actionReciever = objIn;
	}
	
	//actions
	@Override
	public void performAction(Object... args) {
		if (actionReciever != null) {
			IWindowParent p = actionReciever.getWindowParent();
			if (p != null) { p.bringToFront(); }
			actionReciever.actionPerformed(this, args);
		}
	}
	@Override public void onPress() {}
	@Override public boolean runActionOnPress() { return runActionOnPress; }
	@Override public IEnhancedActionObject setRunActionOnPress(boolean value) { runActionOnPress = value; return this; }
	@Override public IEnhancedActionObject setActionReciever(IEnhancedGuiObject objIn) { actionReciever = objIn; return this; }
	@Override public IEnhancedGuiObject getActionReciever() { return actionReciever; }
		
	//objects
	@Override public IEnhancedActionObject setSelectedObject(Object objIn) { storedObject = objIn; return this; }
	@Override public Object getSelectedObject() { return storedObject; }
}
