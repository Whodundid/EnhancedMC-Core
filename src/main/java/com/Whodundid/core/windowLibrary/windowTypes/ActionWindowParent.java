package com.Whodundid.core.windowLibrary.windowTypes;

import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import net.minecraft.client.gui.ScaledResolution;

//Author: Hunter Bragg

public abstract class ActionWindowParent extends WindowParent implements IActionObject {

	protected boolean runActionOnPress = false;
	protected boolean runActionOnRelease = false;
	protected Object storredObject = null;
	protected Object selectedObject = null;
	protected IWindowObject actionReciever;
	
	protected ActionWindowParent(IWindowObject parentIn) {
		actionReciever = parentIn;
		windowInstance = this;
		objectInstance = this;
		res = new ScaledResolution(mc);
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
	@Override public boolean runsActionOnRelease() { return runActionOnRelease; }
	@Override public IActionObject setRunActionOnPress(boolean value) { runActionOnPress = value; return this; }
	@Override public IActionObject setRunActionOnRelease(boolean val) { runActionOnRelease = val; return this; }
	@Override public IActionObject setActionReceiver(IWindowObject objIn) { actionReciever = objIn; return this; }
	@Override public IWindowObject getActionReceiver() { return actionReciever; }
			
	//objects
	@Override public IActionObject setStoredObject(Object objIn) { storredObject = objIn; return this; }
	@Override public Object getStoredObject() { return storredObject; }
	@Override public IActionObject setSelectedObject(Object objIn) { selectedObject = objIn; return this; }
	@Override public Object getSelectedObject() { return selectedObject; }
	
}
