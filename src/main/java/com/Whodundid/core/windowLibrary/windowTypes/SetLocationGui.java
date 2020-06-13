package com.Whodundid.core.windowLibrary.windowTypes;

import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public abstract class SetLocationGui extends WindowParent {
	
	protected StorageBoxHolder<IWindowObject, Boolean> previousStates = new StorageBoxHolder();
	
	protected SetLocationGui hideAllOnRenderer(IWindowObject... exceptionsIn) {
		previousStates.clear();
		EArrayList exceptions = new EArrayList().addA(exceptionsIn);
		
		for (IWindowObject o : EnhancedMCRenderer.getInstance().getAllChildren()) {
			if (o.isPersistent()) { continue; }
			previousStates.add(o, o.isVisible());
		}
		
		previousStates.getObjects().stream().filter(o -> exceptions.notContains(o)).forEach(o -> o.setVisible(false));
		
		return this;
	}
	
	protected SetLocationGui unideAllOnRenderer(IWindowObject... exceptionsIn) {
		EArrayList exceptions = new EArrayList().addA(exceptionsIn);
		
		for (StorageBox<IWindowObject, Boolean> b : previousStates) {
			if (exceptions.notContains(b.getObject())) { b.getObject().setVisible(b.getValue()); }
		}
		
		return this;
	}
	
}
