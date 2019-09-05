package com.Whodundid.core.enhancedGui.guiObjectUtil;

import com.Whodundid.core.enhancedGui.guiUtil.events.ObjectEvent;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.storageUtil.EArrayList;

import java.util.Iterator;

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
		for (IEnhancedGuiObject o : objectIn) {
			if (objects.notContains(o)) { objects.add(o); }
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
	
	public EArrayList<IEnhancedGuiObject> getObjects() { return objects; }
	public EObjectGroup setGroupParent(IEnhancedGuiObject parentIn) { groupParent = parentIn; return this; }
	public IEnhancedGuiObject getGroupParent() { return groupParent; }
}
