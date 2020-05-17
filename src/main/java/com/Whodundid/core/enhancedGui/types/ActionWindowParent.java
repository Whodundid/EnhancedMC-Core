package com.Whodundid.core.enhancedGui.types;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

//Author: Hunter Bragg

public abstract class ActionWindowParent extends WindowParent implements IEnhancedActionObject {

	protected boolean runActionOnPress = false;
	protected Object storredObject = null;
	protected Object selectedObject = null;
	protected IEnhancedGuiObject actionReciever;
	
	protected ActionWindowParent(IEnhancedGuiObject parentIn) {
		actionReciever = parentIn;
	}
	
	//actions
	@Override
	public void performAction(Object... args) {
		if (actionReciever != null) {
			actionReciever.bringToFront();
			actionReciever.actionPerformed(this, args);
		}
	}
	@Override public void onPress() {}
	@Override public boolean runsActionOnPress() { return runActionOnPress; }
	@Override public IEnhancedActionObject setRunActionOnPress(boolean value) { runActionOnPress = value; return this; }
	@Override public IEnhancedActionObject setActionReceiver(IEnhancedGuiObject objIn) { actionReciever = objIn; return this; }
	@Override public IEnhancedGuiObject getActionReceiver() { return actionReciever; }
			
	//objects
	@Override public IEnhancedActionObject setStoredObject(Object objIn) { storredObject = objIn; return this; }
	@Override public Object getStoredObject() { return storredObject; }
	@Override public IEnhancedActionObject setSelectedObject(Object objIn) { selectedObject = objIn; return this; }
	@Override public Object getSelectedObject() { return selectedObject; }
}
