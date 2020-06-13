package com.Whodundid.core.windowLibrary.windowUtil.windowEvents;

import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class ObjectEventHandler {
	
	IWindowObject parent;
	protected EArrayList<IWindowObject> listeners = new EArrayList();
	protected EArrayList<IWindowObject> toBeAdded = new EArrayList();
	protected EArrayList<IWindowObject> toBeRemoved = new EArrayList();
	boolean iterating = false;
	
	public ObjectEventHandler(IWindowObject parentIn) {
		parent = parentIn;
	}
	
	public void processEvent(ObjectEvent e) {
		if (parent.getObjectGroup() != null) { parent.getObjectGroup().notifyGroup(e); }
		sendListenEvent(e);
	}
	
	private void sendListenEvent(ObjectEvent e) {
		iterating = true;
		listeners.forEach(o -> o.onEvent(e));
		iterating = false;
		updateList();
	}
	
	public void unregisterAllObjects() {
		toBeRemoved.addAll(listeners);
		updateList();
	}
	
	public void registerObject(IWindowObject object) {
		if (object != null && listeners.notContains(object)) {
			toBeAdded.add(object);
		}
		updateList();
	}
	
	public void unregisterObject(IWindowObject object) {
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
	
	public EArrayList<IWindowObject> getListenerObjects() { return listeners; }
	
}
