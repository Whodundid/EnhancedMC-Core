package com.Whodundid.core.enhancedGui.guiUtil;

import com.Whodundid.core.enhancedGui.guiUtil.events.ObjectEvent;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class ObjectEventHandler {
	
	IEnhancedGuiObject parent;
	protected EArrayList<IEnhancedGuiObject> listeners = new EArrayList();
	
	public ObjectEventHandler(IEnhancedGuiObject parentIn) {
		parent = parentIn;
	}
	
	public void processEvent(ObjectEvent e) {
		if (parent.getObjectGroup() != null) { parent.getObjectGroup().notifyGroup(e); }
		sendListenEvent(e);
	}
	
	private void sendListenEvent(ObjectEvent e) {
		synchronized (listeners) { listeners.forEach(o -> o.onListen(e)); }
	}
	
	public void unregisterAllObjects() {
		synchronized (listeners) {
			listeners.clear();
		}
	}
	
	public void registerObject(IEnhancedGuiObject object) {
		synchronized (listeners) {
			listeners.addIfNotNullAndNotContains(object);
		}
	}
	
	public void unregisterObject(IEnhancedGuiObject object) {
		synchronized (listeners) {
			listeners.remove(object);
		}
	}
	
	public EArrayList<IEnhancedGuiObject> getListenerObjects() { return listeners; }
}
