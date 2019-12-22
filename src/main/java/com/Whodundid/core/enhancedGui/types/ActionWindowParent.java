package com.Whodundid.core.enhancedGui.types;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

public abstract class ActionWindowParent extends WindowParent implements IEnhancedActionObject {

	protected boolean runActionOnPress = false;
	protected Object storedObject = null;
	protected IEnhancedGuiObject actionReciever;
	
	protected ActionWindowParent(IEnhancedGuiObject parentIn) {
		actionReciever = parentIn;
	}
	
	//actions
	@Override public boolean runActionOnPress() { return runActionOnPress; }
	@Override public IEnhancedActionObject setRunActionOnPress(boolean value) { runActionOnPress = value; return this; }
	@Override public void performAction() {}
	@Override public IEnhancedActionObject setActionReciever(IEnhancedGuiObject objIn) { actionReciever = objIn; return this; }
	@Override public IEnhancedGuiObject getActionReciever() { return actionReciever; }
			
	//objects
	@Override public IEnhancedActionObject setSelectedObject(Object objIn) { storedObject = objIn; return this; }
	@Override public Object getSelectedObject() { return storedObject; }
}
