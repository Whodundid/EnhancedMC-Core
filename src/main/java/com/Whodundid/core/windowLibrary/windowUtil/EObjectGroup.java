package com.Whodundid.core.windowLibrary.windowUtil;

import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.ObjectEvent;
import java.util.Iterator;
import java.util.List;

//Author: Hunter Bragg

public class EObjectGroup {
	
	protected EArrayList<IWindowObject> objects = new EArrayList();
	protected IWindowObject groupParent;
	
	public EObjectGroup() {}
	public EObjectGroup(IWindowObject parentIn) {
		objects.add(parentIn);
		groupParent = parentIn;
	}
	
	/** does not accept duplicates */
	public EObjectGroup addObject(IWindowObject... objectIn) {
		if (objectIn != null) {
			for (IWindowObject o : objectIn) {
				objects.addIfNotNullAndNotContains(o);
			}
		}
		return this;
	}
	
	public EObjectGroup addObjects(List<IWindowObject> objectsIn) {
		if (objectsIn != null) {
			for (IWindowObject o : objectsIn) {
				objects.addIfNotContains(o);
			}
		}
		return this;
	}
	
	public EObjectGroup removeObject(IWindowObject... objectIn) {
		Iterator<IWindowObject> it = objects.iterator();
		while (it.hasNext()) {
			for (IWindowObject o : objectIn) {
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
		for (IWindowObject o : objects) {
			if (o.hasFocus()) { return true; }
		}
		return false;
	}
	
	public boolean isMouseOverAny(int mXIn, int mYIn) {
		for (IWindowObject o : objects) {
			if (o.isMouseOver(mXIn, mYIn)) { return true; }
		}
		return false;
	}
	
	public EObjectGroup setGroupParent(IWindowObject parentIn) { groupParent = parentIn; return this; }
	
	public EArrayList<IWindowObject> getObjects() { return objects; }
	public IWindowObject getGroupParent() { return groupParent; }
}
