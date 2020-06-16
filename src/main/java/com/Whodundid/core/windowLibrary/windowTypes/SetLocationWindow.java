package com.Whodundid.core.windowLibrary.windowTypes;

import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public abstract class SetLocationWindow extends OverlayWindow {
	
	protected StorageBoxHolder<IWindowObject, Boolean> previousStates = new StorageBoxHolder();
	
	protected SetLocationWindow hideAllOnRenderer(IWindowObject... exceptionsIn) {
		previousStates.clear();
		EArrayList exceptions = new EArrayList().addA(exceptionsIn);
		
		for (IWindowObject o : EnhancedMCRenderer.getInstance().getAllChildren()) {
			if (o.isPersistent()) { continue; }
			previousStates.add(o, !o.isHidden());
		}
		
		previousStates.getObjects().stream().filter(o -> exceptions.notContains(o)).forEach(o -> o.setHidden(true));
		
		return this;
	}
	
	protected SetLocationWindow unideAllOnRenderer(IWindowObject... exceptionsIn) {
		EArrayList exceptions = new EArrayList().addA(exceptionsIn);
		
		for (StorageBox<IWindowObject, Boolean> b : previousStates) {
			if (exceptions.notContains(b.getObject())) { b.getObject().setHidden(!b.getValue()); }
		}
		
		return this;
	}
	
}
