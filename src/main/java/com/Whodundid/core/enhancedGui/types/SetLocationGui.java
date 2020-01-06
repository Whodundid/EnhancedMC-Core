package com.Whodundid.core.enhancedGui.types;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;

//Author: Hunter Bragg

public abstract class SetLocationGui extends WindowParent {
	
	protected StorageBoxHolder<IEnhancedGuiObject, Boolean> previousStates = new StorageBoxHolder();
	
	protected SetLocationGui hideAllOnRenderer(IEnhancedGuiObject... exceptionsIn) {
		previousStates.clear();
		EArrayList exceptions = new EArrayList().addA(exceptionsIn);
		
		for (IEnhancedGuiObject o : EnhancedMCRenderer.getInstance().getAllChildren()) {
			if (o.isPersistent()) { continue; }
			previousStates.add(o, o.isVisible());
		}
		
		previousStates.getObjects().stream().filter(o -> exceptions.notContains(o)).forEach(o -> o.setVisible(false));
		
		return this;
	}
	
	protected SetLocationGui unideAllOnRenderer(IEnhancedGuiObject... exceptionsIn) {
		EArrayList exceptions = new EArrayList().addA(exceptionsIn);
		
		for (StorageBox<IEnhancedGuiObject, Boolean> b : previousStates) {
			if (exceptions.notContains(b.getObject())) { b.getObject().setVisible(b.getValue()); }
		}
		
		return this;
	}
}
