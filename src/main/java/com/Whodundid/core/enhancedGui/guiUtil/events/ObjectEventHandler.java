package com.Whodundid.core.enhancedGui.guiUtil.events;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ObjectEventHandler {
	
	IEnhancedGuiObject parent;
	protected EArrayList<IEnhancedGuiObject> listeners = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> toBeAdded = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> toBeRemoved = new EArrayList();
	boolean iterating = false;
	
	public ObjectEventHandler(IEnhancedGuiObject parentIn) {
		parent = parentIn;
	}
	
	public void processEvent(ObjectEvent e) {
		if (parent.getObjectGroup() != null) { parent.getObjectGroup().notifyGroup(e); }
		sendListenEvent(e);
	}
	
	private void sendListenEvent(ObjectEvent e) {
		iterating = true;
		listeners.forEach(o -> o.onListen(e));
		iterating = false;
		updateList();
	}
	
	public void unregisterAllObjects() {
		toBeRemoved.addAll(listeners);
		updateList();
	}
	
	public void registerObject(IEnhancedGuiObject object) {
		if (object != null && listeners.notContains(object)) {
			toBeAdded.add(object);
		}
		updateList();
	}
	
	public void unregisterObject(IEnhancedGuiObject object) {
		if (object != null) {
			toBeRemoved.add(object);
		}
		updateList();
	}
	
	private void updateList() {
		if (!iterating) {
			if (toBeAdded.isNotEmpty()) { listeners.addAll(toBeAdded); toBeAdded.clear(); }
			if (toBeRemoved.isNotEmpty()) { listeners.removeAll(toBeRemoved); toBeRemoved.clear(); }
		}
	}
	
	public EArrayList<IEnhancedGuiObject> getListenerObjects() { return listeners; }
}
