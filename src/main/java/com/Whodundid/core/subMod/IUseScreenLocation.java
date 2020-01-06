package com.Whodundid.core.subMod;

import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.StorageBox;

//Author: Hunter Bragg

public interface IUseScreenLocation {
	
	public void setLocation(ScreenLocation locIn);
	public void setLocation(int xIn, int yIn);
	public StorageBox<Integer, Integer> getLocation();
	public ScreenLocation getScreenLocation();
	public IWindowParent getScreenLocationGui();
}
