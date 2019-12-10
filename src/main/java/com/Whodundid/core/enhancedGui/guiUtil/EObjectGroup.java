package com.Whodundid.core.enhancedGui.guiUtil;

import com.Whodundid.core.enhancedGui.objectEvents.ObjectEvent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.storageUtil.EArrayList;

import java.util.Iterator;
import java.util.List;

//Last edited: Jan 10, 2019
//First Added: Jan 10, 2019
//Author: Hunter Bragg

public class EObjectGroup {
	
	protected EArrayList<IEnhancedGuiObject> objects = new EArrayList();
	protected IEnhancedGuiObject groupParent;
	
	public EObjectGroup() {}
	public EObjectGroup(IEnhancedGuiObject parentIn) {
		objects.add(parentIn);
		groupParent = parentIn;
	}
	
	/** does not accept duplicates */
	public EObjectGroup addObject(IEnhancedGuiObject... objectIn) {
		if (objectIn != null) {
			for (IEnhancedGuiObject o : objectIn) {
				objects.addIfNotContains(o);
			}
		}
		return this;
	}
	
	public EObjectGroup addObjects(List<IEnhancedGuiObject> objectsIn) {
		if (objectsIn != null) {
			for (IEnhancedGuiObject o : objectsIn) {
				objects.addIfNotContains(o);
			}
		}
		return this;
	}
	
	public EObjectGroup removeObject(IEnhancedGuiObject... objectIn) {
		Iterator<IEnhancedGuiObject> it = objects.iterator();
		while (it.hasNext()) {
			for (IEnhancedGuiObject o : objectIn) {
				if (o.equals(it.next())) { it.remove(); }
			}
		}
		return this;
	}
	
	public void notifyGroup(ObjectEvent e) {
		objects.forEach((o) -> {
			//System.out.println("group parent: " + e.getEventParent() + " " + o.equals(e.getEventParent()));
			if (!o.equals(e.getEventParent())) {
				o.onGroupNotification(e);
			}
		});
	}
	
	public boolean doAnyHaveFocus() {
		for (IEnhancedGuiObject o : objects) {
			if (o.hasFocus()) { return true; }
		}
		return false;
	}
	
	public boolean isMouseOverAny(int mXIn, int mYIn) {
		for (IEnhancedGuiObject o : objects) {
			if (o.isMouseOver(mXIn, mYIn)) { return true; }
		}
		return false;
	}
	
	public EObjectGroup setGroupParent(IEnhancedGuiObject parentIn) { groupParent = parentIn; return this; }
	
	public EArrayList<IEnhancedGuiObject> getObjects() { return objects; }
	public IEnhancedGuiObject getGroupParent() { return groupParent; }
}
