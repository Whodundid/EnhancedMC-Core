package com.Whodundid.core.enhancedGui.types;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;

public abstract class SetLocationGui extends InnerEnhancedGui {
	
	protected StorageBoxHolder<IEnhancedGuiObject, Boolean> previousStates = new StorageBoxHolder();
	
	protected SetLocationGui() { super(); }
	protected SetLocationGui(Object oldGuiIn) { super(oldGuiIn); }
	protected SetLocationGui(IEnhancedGuiObject parentIn) { super(parentIn); }
	protected SetLocationGui(IEnhancedGuiObject parentIn, Object oldGuiIn) { super(parentIn, oldGuiIn); }
	protected SetLocationGui(IEnhancedGuiObject parentIn, int posX, int posY) { super(parentIn, posX, posY); }
	protected SetLocationGui(IEnhancedGuiObject parentIn, int posX, int posY, Object oldGuiIn) { super(parentIn, posX, posY, oldGuiIn); }
	
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
