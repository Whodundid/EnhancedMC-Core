package com.Whodundid.core.app;

import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowParent;

//Author: Hunter Bragg

public interface IUseScreenLocation {
	
	public void setLocation(ScreenLocation locIn);
	public void setLocation(int xIn, int yIn);
	public StorageBox<Integer, Integer> getLocation();
	public ScreenLocation getScreenLocation();
	public IWindowParent getScreenLocationGui();
	
}
